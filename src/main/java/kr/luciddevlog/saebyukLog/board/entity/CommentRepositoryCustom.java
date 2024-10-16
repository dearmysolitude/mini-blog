package kr.luciddevlog.saebyukLog.board.entity;

import kr.luciddevlog.saebyukLog.board.dto.CommentItemWithAuthorName;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentItemWithAuthorName> findCommentItemsByRootId(Long rootId);
}
