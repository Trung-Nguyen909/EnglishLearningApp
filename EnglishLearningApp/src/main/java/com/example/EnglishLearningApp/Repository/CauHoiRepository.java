package com.example.EnglishLearningApp.Repository;

import com.example.EnglishLearningApp.Model.CauHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauHoiRepository extends JpaRepository<CauHoi, Integer> {
    @Query(
            value = "SELECT ch.* " +
                    "FROM CauHoi ch " +
                    "JOIN BaiTap bt ON bt.ID = ch.IDBaiTap " +
                    "JOIN CapDo c ON c.ID = ch.IDCapDo " +
                    "WHERE c.ID = :idCapDo AND bt.ID = :idBaiHoc",
            nativeQuery = true
    )
    List<CauHoi> getCauHoiByBaiHocAndCapDo(
            @Param("idBaiHoc") int idBaiHoc,
            @Param("idCapDo") int idCapDo
    );

}
