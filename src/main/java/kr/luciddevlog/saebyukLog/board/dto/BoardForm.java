package kr.luciddevlog.saebyukLog.board.dto;

import jakarta.validation.constraints.NotBlank;
import kr.luciddevlog.saebyukLog.board.entity.BoardCategory;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import lombok.*;

@Getter
@Setter // Spring 바인딩은 Setter사용, set 접두사로 된 메서드 찾는다
@NoArgsConstructor
public class BoardForm {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private Long memberId;
    private Long rootId;
    private BoardCategory category;

    @Builder
    public BoardForm(String title, String content, Long memberId, Long rootId, BoardCategory category) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.rootId = rootId;
        this.category = category;
    }

    public void addAuthor(Long id) {
        this.memberId = id;
    }
    public void setCategory(String category) {
        if(category.equalsIgnoreCase("notice")) { // 대소문자 구분 없이
            this.category = BoardCategory.NOTICE;
        } else {
            this.category = BoardCategory.REVIEW;
        }
    }

    @Override
    public String toString() {
        return "BoardForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", memberId=" + memberId +
                ", rootId=" + rootId +
                ", category=" + category +
                '}';
    }

}
