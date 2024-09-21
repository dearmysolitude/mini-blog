package kr.luciddevlog.saebyukLog.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentForm {
    private String content;
    private Integer reLevel;
    private Long rootId; // 댓글이 달리느 ㄴ게시글
    private Long memberId; // 게시글을 다는 멤버의 아이디
    private Long parentId; // 원본 댓글

    public void addMember(Long id) {
        this.memberId = id;
    }
}
