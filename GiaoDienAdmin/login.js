const API_BASE = "http://localhost:8080";
const TOKEN_KEY = "english_admin_jwt";

const $ = (s, r = document) => r.querySelector(s);

function safeJsonParse(s, fallback = null) {
  try {
    return JSON.parse(s);
  } catch {
    return fallback;
  }
}

async function http(url, { method = "GET", body } = {}) {
  const res = await fetch(url, {
    method,
    headers: body ? { "Content-Type": "application/json" } : {},
    body: body ? JSON.stringify(body) : undefined,
  });

  const text = await res.text();
  const data = text ? safeJsonParse(text, text) : null;

  if (!res.ok) {
    console.error("LOGIN HTTP ERROR:", res.status, data);
    throw new Error(
      typeof data === "string" ? data : data?.message || `HTTP ${res.status}`
    );
  }
  return data;
}

function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

function getToken() {
  return localStorage.getItem(TOKEN_KEY) || "";
}

function showMsg(msg) {
  const el = $("#hint") || $("#loginMsg");
  if (el) el.textContent = msg;
  else alert(msg);
}

function extractToken(data) {
  // ✅ thử nhiều dạng response
  return (
    data?.token ||
    data?.accessToken ||
    data?.jwt ||
    data?.jwtToken ||
    data?.result?.token ||
    data?.result?.accessToken ||
    data?.data?.token ||
    data?.data?.accessToken
  );
}

async function doLogin() {
  const email = String($("#email")?.value || "").trim();
  const matkhau = String($("#password")?.value || "").trim();

  if (!email || !matkhau) {
    showMsg("Vui lòng nhập email và mật khẩu.", "bad");
    return;
  }

  try {
    const data = await http(`${API_BASE}/user/login`, {
      method: "POST",
      body: { email, matkhau },
    });

    console.log("LOGIN RESPONSE:", data);

    const token =
      data?.token ||
      data?.accessToken ||
      data?.jwt ||
      data?.result?.token ||
      data?.result?.accessToken;

    if (!token) {
      showMsg("Đăng nhập thành công nhưng không tìm thấy token.", "bad");
      return;
    }

    setToken(token);
    showMsg("Đăng nhập thành công ", "ok");

    window.location.replace("index.html");
  } catch (e) {
    showMsg("Đăng nhập thất bại: " + e.message, "bad");
  }
}

document.addEventListener("DOMContentLoaded", () => {
  if (getToken()) {
    window.location.replace("index.html");
    return;
  }

  $("#loginForm")?.addEventListener("submit", (e) => {
    e.preventDefault();
    doLogin();
  });

  $("#password")?.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      doLogin();
    }
  });
});
