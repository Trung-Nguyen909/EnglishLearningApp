package com.example.EnglishLearningApp.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "NguoiDung")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "tenDangNhap", nullable = false, unique = true, length = 50)
    private String tenDangNhap;

    @Column(name = "matKhau", nullable = false, length = 100)
    private String matKhau;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "LastLogin")
    private LocalDateTime lastLogin;

    @Builder.Default
    @Column(name = "Streak")
    private Integer streak = 0;

    @Builder.Default
    @Column(name = "role", length = 30)
    private String role = "USER";

    @Column(name = "TongThoiGianHoatDong")
    private int tongThoiGianHoatDong;

    @OneToMany(mappedBy = "nguoiDung")
    @JsonManagedReference
    private Set<NguoiDungThanhTich> thanhTichs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TuVungYeuThich",
            joinColumns = @JoinColumn(name = "IDNguoiDung"),
            inverseJoinColumns = @JoinColumn(name = "IDTuVung")
    )
    @JsonManagedReference
    private Set<TuVung> tuVungs = new HashSet<>();
}