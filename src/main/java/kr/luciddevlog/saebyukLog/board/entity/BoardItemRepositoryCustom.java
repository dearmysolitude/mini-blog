package kr.luciddevlog.saebyukLog.board.entity;

import kr.luciddevlog.saebyukLog.board.dto.BoardItemWithAuthorName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardItemRepositoryCustom {
    Page<BoardItemWithAuthorName> findBoardItemsByCategory(BoardCategory category, Pageable pageable);
    BoardItemWithAuthorName findBoardItemByIdWithUserName(Long id);
}
