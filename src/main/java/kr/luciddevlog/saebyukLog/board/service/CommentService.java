package kr.luciddevlog.saebyukLog.board.service;


import kr.luciddevlog.saebyukLog.board.dto.CommentForm;
import kr.luciddevlog.saebyukLog.board.dto.CommentItemWithAuthorName;
import kr.luciddevlog.saebyukLog.board.entity.BoardItem;

import java.util.List;

public interface CommentService {
    List<CommentItemWithAuthorName> showContent(Long rootId, Long userId);
    // 댓글 crud
    void createComment(CommentForm commentForm);
    Long updateCommentItem(Long id, String content);
    Long deleteCommentItem(Long id);
    BoardItem getCommentInfo(Long id);
}
