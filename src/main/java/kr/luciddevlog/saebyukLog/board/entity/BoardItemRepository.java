package kr.luciddevlog.saebyukLog.board.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Refactor: QueryDSL로 변경
@Repository
public interface BoardItemRepository extends JpaRepository<BoardItem, Long>, BoardItemRepositoryCustom {
    Long countByCategoryAndRootIdIsNull(BoardCategory boardCategory);

    @Modifying
    @Query(value = "UPDATE BoardItem SET viewCnt = viewCnt + 1 WHERE id = :id")
    void incrementViewCount(@Param("id") Long id);
}
