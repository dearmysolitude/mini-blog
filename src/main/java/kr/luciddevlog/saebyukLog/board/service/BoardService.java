package kr.luciddevlog.saebyukLog.board.service;

import kr.luciddevlog.saebyukLog.board.dto.BoardForm;
import kr.luciddevlog.saebyukLog.board.dto.BoardItemWithAuthorName;
import kr.luciddevlog.saebyukLog.board.dto.Pagination;
import kr.luciddevlog.saebyukLog.board.entity.BoardCategory;
import kr.luciddevlog.saebyukLog.board.entity.BoardItem;
import org.springframework.data.domain.Page;

public interface BoardService {
	// 페이지네이션
	Page<BoardItemWithAuthorName> searchContents(BoardCategory boardCategory, int sizePerPage, int currentPage);
	Pagination makePageInfo(BoardCategory boardCategory, int sizePerPage, int currentPage);

	// 게시글 crud
	BoardItemWithAuthorName showSingleContent(Long id, Long userId);
	BoardItem createBoard(BoardForm boardForm, Long memberId);
	void updateBoardItem(Long id, BoardForm form);
	void deleteBoardItem(Long id);

	// 댓글
	void updateViewCount(Long id);
	BoardItem getBoardItem(Long id);
}