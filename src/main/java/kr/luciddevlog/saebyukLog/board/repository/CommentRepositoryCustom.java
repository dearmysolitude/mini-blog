package kr.luciddevlog.saebyukLog.board.repository;

import kr.luciddevlog.saebyukLog.board.dto.CommentItemWithAuthorName;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentItemWithAuthorName> findCommentItemsByRootId(Long rootId);
}
