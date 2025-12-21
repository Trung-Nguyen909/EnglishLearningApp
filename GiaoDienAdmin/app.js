/* =========================
   CONFIG
========================= */
const API_BASE = "http://localhost:8080";
const TOKEN_KEY = "english_admin_jwt";

/* =========================
   DOM UTILS
========================= */
const $ = (s, r = document) => r.querySelector(s);
const $$ = (s, r = document) => Array.from(r.querySelectorAll(s));

function escapeHtml(s) {
  return String(s ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function safeJsonParse(s, fallback = null) {
  try {
    return JSON.parse(s);
  } catch {
    return fallback;
  }
}

/* =========================
   TOAST
========================= */
function toast(title, desc = "", type = "info") {
  const wrap = $("#toastWrap");
  if (!wrap) {
    alert(`${title}\n${desc}`);
    return;
  }

  const el = document.createElement("div");
  el.className = "toast";
  el.innerHTML = `
    <div class="toast__title">${escapeHtml(title)}</div>
    <div class="toast__desc">${escapeHtml(desc)}</div>
  `;

  if (type === "ok") el.style.borderColor = "rgba(62,240,177,.45)";
  if (type === "warn") el.style.borderColor = "rgba(255,207,90,.55)";
  if (type === "bad") el.style.borderColor = "rgba(255,107,139,.55)";

  wrap.appendChild(el);
  setTimeout(() => el.remove(), 2600);
}

/* =========================
   TOKEN
========================= */
function getToken() {
  return localStorage.getItem(TOKEN_KEY) || "";
}
function clearToken() {
  localStorage.removeItem(TOKEN_KEY);
}
function requireTokenOrGoLogin() {
  if (!getToken()) window.location.href = "login.html";
}

/* =========================
   HTTP (debug on)
========================= */
async function http(url, { method = "GET", body } = {}) {
  const token = getToken();

  const reqInit = {
    method,
    headers: {
      ...(body ? { "Content-Type": "application/json" } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  };

  console.log("[HTTP]", method, url, body || "");

  const res = await fetch(url, reqInit);
  const raw = await res.text().catch(() => "");
  const data = raw ? safeJsonParse(raw, raw) : null;

  console.log("[HTTP RES]", res.status, url, data);

  if (res.status === 401 || res.status === 403) {
    toast("Phiên đăng nhập hết hạn", "Vui lòng đăng nhập lại", "warn");
    clearToken();
    setTimeout(() => (window.location.href = "login.html"), 600);
    throw new Error(`HTTP ${res.status}: Unauthorized`);
  }

  if (res.status === 204) return null;

  if (!res.ok) {
    const msg =
      typeof data === "string"
        ? data
        : data?.message || data?.error || "Request failed";
    throw new Error(`HTTP ${res.status}: ${msg}`);
  }

  return data;
}

/* =========================
   ROUTES (chỉ 3 module)
========================= */
const ApiRoutes = {
  courses: {
    list: () => `${API_BASE}/KhoaHoc`,
    create: () => `${API_BASE}/KhoaHoc`,
    update: (id) => `${API_BASE}/KhoaHoc/${id}`,
    remove: (id) => `${API_BASE}/KhoaHoc/${id}`,
  },
  lessons: {
    list: () => `${API_BASE}/baihoc`,
    create: () => `${API_BASE}/baihoc`,
    update: (id) => `${API_BASE}/baihoc/${id}`,
    remove: (id) => `${API_BASE}/baihoc/${id}`,
  },
  questions: {
    list: () => `${API_BASE}/cauhoi`,
    create: () => `${API_BASE}/cauhoi`,
    update: (id) => `${API_BASE}/cauhoi/${id}`,
    remove: (id) => `${API_BASE}/cauhoi/${id}`,
  },
};

/* =========================
   DATA LAYER
========================= */
const Data = {
  async list(entity) {
    return await http(ApiRoutes[entity].list());
  },
  async create(entity, payload) {
    return await http(ApiRoutes[entity].create(), {
      method: "POST",
      body: payload,
    });
  },

  // ✅ UPDATE: fallback nhiều endpoint/method cho lessons/questions
  async update(entity, id, payload) {
    const idNum = Number(id);
    const body = { ...payload, id: idNum };

    const mainUrl = ApiRoutes[entity].update(id);
    const baseUrl = ApiRoutes[entity].create();

    // Courses: ưu tiên PUT chuẩn
    if (entity === "courses") {
      return await http(mainUrl, { method: "PUT", body });
    }

    // Lessons/Questions: thử nhiều kiểu
    const pathName = entity === "lessons" ? "baihoc" : "cauhoi";

    const candidates = [
      { url: mainUrl, method: "PUT" },
      { url: mainUrl, method: "PATCH" },

      // update không cần /{id}, lấy id trong body
      { url: baseUrl, method: "PUT" },
      { url: baseUrl, method: "PATCH" },

      // một số backend dùng POST update
      { url: mainUrl, method: "POST" },
      { url: baseUrl, method: "POST" },

      // biến thể hay gặp
      { url: `${API_BASE}/${pathName}/update/${idNum}`, method: "PUT" },
      { url: `${API_BASE}/${pathName}/update`, method: "POST" },
    ];

    let lastErr = null;

    for (const c of candidates) {
      try {
        console.log("[TRY UPDATE]", entity, c.method, c.url, body);
        const res = await http(c.url, { method: c.method, body });
        console.log("[UPDATE OK]", entity, c.method, c.url, res);
        return res;
      } catch (e) {
        lastErr = e;
        console.warn("[UPDATE FAIL]", entity, c.method, c.url, e.message);
      }
    }

    throw lastErr || new Error("Không cập nhật được");
  },

  async remove(entity, id) {
    await http(ApiRoutes[entity].remove(id), { method: "DELETE" });
    return true;
  },
};

/* =========================
   MODAL
========================= */
const Modal = (() => {
  let currentSubmit = null;
  let currentFields = [];

  function open({
    title,
    sub,
    fields,
    initial = {},
    onSubmit,
    submitText = "Lưu",
  }) {
    $("#modalTitle").textContent = title || "Modal";
    $("#modalSub").textContent = sub || "";
    $("#btnModalSubmit").textContent = submitText;

    currentSubmit = onSubmit || null;
    currentFields = fields || [];

    const form = $("#modalForm");
    form.innerHTML = "";

    for (const f of currentFields) {
      const wrap = document.createElement("div");
      wrap.className = f.col12 ? "field col-12" : "field";
      wrap.innerHTML = `
        <label>${escapeHtml(f.label)}</label>
        ${renderInput(f, initial[f.name])}
      `;
      form.appendChild(wrap);
    }

    $("#modalBackdrop").classList.remove("hidden");
    queueMicrotask(() =>
      form.querySelector("input,select,textarea")?.focus?.()
    );
  }

  function close() {
    $("#modalBackdrop").classList.add("hidden");
    $("#modalForm").innerHTML = "";
    currentSubmit = null;
    currentFields = [];
    $("#btnModalSubmit").textContent = "Lưu";
  }

  function bindOnce() {
    $("#btnModalClose")?.addEventListener("click", close);
    $("#btnModalCancel")?.addEventListener("click", close);
    $("#modalBackdrop")?.addEventListener("click", (e) => {
      if (e.target?.id === "modalBackdrop") close();
    });
    $("#btnModalSubmit")?.addEventListener("click", () =>
      $("#modalForm").requestSubmit()
    );

    $("#modalForm")?.addEventListener("submit", (e) => {
      e.preventDefault();

      const data = {};
      for (const f of currentFields) {
        const el = $("#modalForm").querySelector(`[name="${f.name}"]`);
        if (!el) continue;

        let v = el.value;

        if (f.type === "number") v = v === "" ? null : Number(v);
        data[f.name] = v;
      }

      currentSubmit?.(data);
    });

    document.addEventListener("keydown", (e) => {
      const opened = !$("#modalBackdrop")?.classList.contains("hidden");
      if (opened && e.key === "Escape") close();
    });
  }

  function renderInput(f, value) {
    const v = value ?? "";

    if (f.type === "select") {
      const opts = (f.options ?? [])
        .map((o) => {
          const val = typeof o === "object" ? o.value : o;
          const label = typeof o === "object" ? o.label : o;
          const selected = String(val) === String(v) ? "selected" : "";
          return `<option value="${escapeHtml(val)}" ${selected}>${escapeHtml(
            label
          )}</option>`;
        })
        .join("");

      return `<select name="${f.name}" class="input" ${
        f.required ? "required" : ""
      }>${opts}</select>`;
    }

    if (f.as === "textarea") {
      return `<textarea
        name="${f.name}"
        class="input textarea"
        placeholder="${escapeHtml(f.placeholder || "")}"
        ${f.required ? "required" : ""}>${escapeHtml(v)}</textarea>`;
    }

    return `<input
      name="${f.name}"
      class="input"
      type="${escapeHtml(f.type || "text")}"
      value="${escapeHtml(v)}"
      placeholder="${escapeHtml(f.placeholder || "")}"
      ${f.required ? "required" : ""} />`;
  }

  return { open, close, bindOnce };
})();

/* =========================
   PAGE CONFIG
========================= */
const PageCfg = {
  courses: {
    title: "Khóa học",
    sub: "Quản lý khóa học",
    entity: "courses",
    columns: [
      { key: "id", label: "ID" },
      { key: "tenKhoaHoc", label: "Tên khóa học" },
      { key: "trinhDo", label: "Trình độ" },
      { key: "ngayTao", label: "Ngày tạo" },
    ],
    fields: [
      {
        name: "tenKhoaHoc",
        label: "Tên khóa học",
        required: true,
        col12: true,
      },
      {
        name: "trinhDo",
        label: "Trình độ",
        type: "select",
        options: ["Beginner", "Intermediate", "Advanced"],
        required: true,
      },
      { name: "ngayTao", label: "Ngày tạo", type: "date", required: true },
      { name: "moTa", label: "Mô tả", as: "textarea", col12: true },
    ],
  },

  lessons: {
    title: "Bài học",
    sub: "Quản lý bài học",
    entity: "lessons",
    columns: [
      { key: "id", label: "ID" },
      { key: "idKhoaHoc", label: "ID Khóa học" },
      { key: "tenBaiHoc", label: "Tên bài học" },
      { key: "thuTuBaiHoc", label: "Thứ tự" },
      { key: "trangThai", label: "Trạng thái" },
    ],
    fields: [
      {
        name: "idKhoaHoc",
        label: "ID Khóa học",
        type: "number",
        required: true,
      },
      { name: "tenBaiHoc", label: "Tên bài học", required: true, col12: true },
      { name: "moTa", label: "Mô tả", as: "textarea", col12: true },
      { name: "noiDung", label: "Nội dung", as: "textarea", col12: true },
      { name: "thuTuBaiHoc", label: "Thứ tự bài học", type: "number" },
      {
        name: "trangThai",
        label: "Trạng thái",
        type: "select",
        options: ["Đã hoàn thành", "Đang Hoàn thành", "Chưa làm"],
      },
    ],
  },

  questions: {
    title: "Câu hỏi",
    sub: "Quản lý câu hỏi",
    entity: "questions",
    columns: [
      { key: "id", label: "ID" },
      { key: "idCapDo", label: "Cấp độ" },
      { key: "idBaiTap", label: "ID Bài tập" },
      { key: "loaiCauHoi", label: "Loại" },
      { key: "noiDungCauHoi", label: "Nội dung" },
      { key: "audioUrl", label: "Audio" },
    ],
    fields: [
      { name: "idCapDo", label: "ID Cấp độ", type: "number", required: true },
      { name: "idBaiTap", label: "ID Bài tập (có thể trống)", type: "number" },
      {
        name: "loaiCauHoi",
        label: "Loại câu hỏi",
        type: "select",
        options: ["ABCD", "Listening", "Speaking", "FillBlank"],
        required: true,
      },
      {
        name: "noiDungCauHoi",
        label: "Nội dung câu hỏi",
        as: "textarea",
        required: true,
        col12: true,
      },
      {
        name: "duLieuDapAn",
        label: "Dữ liệu đáp án (JSON STRING)",
        as: "textarea",
        required: true,
        col12: true,
        placeholder: `{"A":"...","B":"...","C":"...","D":"...","Correct":"A"}`,
      },
      { name: "giaiThich", label: "Giải thích", as: "textarea", col12: true },
      {
        name: "audioUrl",
        label: "Audio URL",
        placeholder: "vd: dad.mp3",
        col12: true,
      },
    ],
  },
};

/* =========================
   NORMALIZE
========================= */
function cleanEmptyToNull(obj) {
  const p = { ...obj };
  for (const k of Object.keys(p)) {
    if (typeof p[k] === "string") p[k] = p[k].trim();
    if (p[k] === "") p[k] = null;
  }
  return p;
}

function normalizePayload(route, data, idForUpdate = null) {
  let p = cleanEmptyToNull(data);

  if (idForUpdate != null) p.id = Number(idForUpdate);

  if (route === "courses") {
    if (!p.tenKhoaHoc) throw new Error("tenKhoaHoc không được trống");
    if (!p.trinhDo) throw new Error("trinhDo không được trống");
    if (!p.ngayTao) throw new Error("ngayTao không được trống");
  }

  if (route === "lessons") {
    p.idKhoaHoc = p.idKhoaHoc == null ? null : Number(p.idKhoaHoc);
    p.thuTuBaiHoc = p.thuTuBaiHoc == null ? null : Number(p.thuTuBaiHoc);

    if (p.idKhoaHoc == null) throw new Error("idKhoaHoc không được trống");
    if (!p.tenBaiHoc) throw new Error("tenBaiHoc không được trống");
  }

  if (route === "questions") {
    p.idCapDo = p.idCapDo == null ? null : Number(p.idCapDo);
    p.idBaiTap = p.idBaiTap == null ? null : Number(p.idBaiTap);

    if (p.idCapDo == null) throw new Error("idCapDo không được trống");
    if (!p.noiDungCauHoi) throw new Error("noiDungCauHoi không được trống");
    if (!p.loaiCauHoi) throw new Error("loaiCauHoi không được trống");

    if (p.duLieuDapAn == null) {
      throw new Error("duLieuDapAn không được để trống (DB NOT NULL)");
    }

    // validate JSON string
    if (typeof p.duLieuDapAn === "object")
      p.duLieuDapAn = JSON.stringify(p.duLieuDapAn);
    try {
      JSON.parse(String(p.duLieuDapAn));
      p.duLieuDapAn = String(p.duLieuDapAn);
    } catch {
      throw new Error("duLieuDapAn phải là JSON hợp lệ (string JSON).");
    }
  }

  return p;
}

/* =========================
   RENDER / STATE
========================= */
let currentRoute = "courses";
let allRows = [];

function setActiveNav(route) {
  currentRoute = route;

  $$(".nav__item").forEach((b) =>
    b.classList.toggle("is-active", b.dataset.route === route)
  );

  $("#pageTitle").textContent = PageCfg[route].title;
  $("#pageSub").textContent = PageCfg[route].sub;
  $("#searchInput").value = "";
}

function getIdValue(row) {
  return row?.id ?? row?.ID;
}
function getValue(row, col) {
  if (!row) return "";
  if (col.key in row) return row[col.key];
  return "";
}

function renderTable(rows) {
  const cfg = PageCfg[currentRoute];

  const html = `
    <table class="table" id="dataTable">
      <thead>
        <tr>
          ${cfg.columns.map((c) => `<th>${escapeHtml(c.label)}</th>`).join("")}
          <th style="text-align:right">Hành động</th>
        </tr>
      </thead>
      <tbody>
        ${rows
          .map((r) => {
            const id = getIdValue(r);
            const tds = cfg.columns
              .map((c) => `<td>${escapeHtml(getValue(r, c))}</td>`)
              .join("");

            return `
              <tr data-id="${escapeHtml(id)}">
                ${tds}
                <td>
                  <div class="actions">
                    <button class="btn small btnEdit">Sửa</button>
                    <button class="btn small danger btnDel">Xóa</button>
                  </div>
                </td>
              </tr>
            `;
          })
          .join("")}
      </tbody>
    </table>
  `;

  $("#tableWrap").innerHTML = html;
}

async function loadAndRender() {
  const cfg = PageCfg[currentRoute];
  allRows = await Data.list(cfg.entity);
  renderTable(allRows);
}

/* =========================
   EVENTS
========================= */
function bindEvents() {
  // nav
  $$(".nav__item").forEach((btn) => {
    btn.addEventListener("click", async () => {
      try {
        setActiveNav(btn.dataset.route);
        await loadAndRender();
      } catch (e) {
        toast("Lỗi tải dữ liệu", e.message || "API error", "bad");
      }
    });
  });

  // search
  $("#searchInput")?.addEventListener("input", (e) => {
    const q = String(e.target.value || "")
      .toLowerCase()
      .trim();
    if (!q) return renderTable(allRows);
    renderTable(
      allRows.filter((r) => JSON.stringify(r).toLowerCase().includes(q))
    );
  });

  // add
  $("#btnAdd")?.addEventListener("click", () => {
    const cfg = PageCfg[currentRoute];

    const initial =
      currentRoute === "questions"
        ? {
            duLieuDapAn: `{"A":"...","B":"...","C":"...","D":"...","Correct":"A"}`,
          }
        : {};

    Modal.open({
      title: `Thêm ${cfg.title}`,
      sub: cfg.sub,
      fields: cfg.fields,
      initial,
      onSubmit: async (data) => {
        try {
          const payload = normalizePayload(currentRoute, data);
          await Data.create(cfg.entity, payload);
          toast("Đã thêm", "OK", "ok");
          Modal.close();
          await loadAndRender();
        } catch (err) {
          toast("Thêm thất bại", err.message || "Error", "bad");
        }
      },
    });
  });

  // edit/delete delegation
  document.addEventListener("click", async (e) => {
    const tr = e.target.closest("tr[data-id]");
    if (!tr) return;

    const id = tr.getAttribute("data-id");
    const cfg = PageCfg[currentRoute];
    const entity = cfg.entity;

    // edit
    if (e.target.closest(".btnEdit")) {
      const item = allRows.find((x) => String(getIdValue(x)) === String(id));
      if (!item) return;

      const initial = {};
      cfg.fields.forEach((f) => (initial[f.name] = item[f.name] ?? null));

      if (currentRoute === "questions" && !initial.duLieuDapAn) {
        initial.duLieuDapAn = `{"A":"...","B":"...","C":"...","D":"...","Correct":"A"}`;
      }

      Modal.open({
        title: `Sửa ${cfg.title}`,
        sub: `ID=${id}`,
        fields: cfg.fields,
        initial: { ...item, ...initial },
        onSubmit: async (data) => {
          try {
            const payload = normalizePayload(currentRoute, data, id);
            await Data.update(entity, id, payload);
            toast("Đã cập nhật", `ID=${id}`, "ok");
            Modal.close();
            await loadAndRender();
          } catch (err) {
            toast("Cập nhật thất bại", err.message || "Error", "bad");
          }
        },
      });
      return;
    }

    // delete
    if (e.target.closest(".btnDel")) {
      const ok = confirm(`Xóa ID=${id}?`);
      if (!ok) return;

      try {
        await Data.remove(entity, id);
        toast("Đã xóa", `ID=${id}`, "warn");
        await loadAndRender();
      } catch (err) {
        toast("Xóa thất bại", err.message || "Error", "bad");
      }
      return;
    }
  });

  // logout
  $("#btnLogout")?.addEventListener("click", () => {
    clearToken();
    toast("Đăng xuất", "OK", "ok");
    setTimeout(() => (window.location.href = "login.html"), 250);
  });
}

/* =========================
   INIT
========================= */
document.addEventListener("DOMContentLoaded", async () => {
  requireTokenOrGoLogin();
  Modal.bindOnce();
  bindEvents();

  setActiveNav("courses");

  try {
    await loadAndRender();
  } catch (e) {
    toast(
      "Không gọi được API",
      e.message || "Kiểm tra backend/CORS/Security",
      "bad"
    );
  }
});
