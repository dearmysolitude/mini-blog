package kr.luciddevlog.saebyukLog.board.service;

import kr.luciddevlog.saebyukLog.board.dto.CommentDto;
import kr.luciddevlog.saebyukLog.board.dto.CommentForm;
import kr.luciddevlog.saebyukLog.board.dto.CommentItemWithAuthorName;
import kr.luciddevlog.saebyukLog.board.entity.BoardCategory;
import kr.luciddevlog.saebyukLog.board.entity.BoardItem;
import kr.luciddevlog.saebyukLog.board.entity.CommentRepository;
import kr.luciddevlog.saebyukLog.user.entity.UserItem;
import kr.luciddevlog.saebyukLog.user.entity.UserItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final UserItemRepository memberRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserItemRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    // 대댓글에 대한 Indent를 전달할 dto필요 혹은 프론트에서 reLevel에 따라 들여써야 함
    public List<CommentItemWithAuthorName> showContent(Long rootId, Long userId) {
        List<CommentItemWithAuthorName> comments = commentRepository.findCommentItemsByRootId(rootId);

        if (userId == null) {
            return comments;
        }

        return comments.stream()
                .map(commentItemWithAuthorName -> new CommentDto(commentItemWithAuthorName, userId))
                .collect(Collectors.toList());
    }

    // 댓글의 순서를 부여하는 것이 주요 로직
    @Transactional
    public void createComment(CommentForm commentForm) {
        String content = commentForm.getContent();
        Long rootId = commentForm.getRootId();
        Long parentId = commentForm.getParentId();
        UserItem member = memberRepository.findById(commentForm.getMemberId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않음")
        );

        if(content == null || rootId == null) {
            throw new IllegalArgumentException("인자가 잘못 전달됨");
        }

        if(parentId == -1) { // 새 코멘트 작성
            BoardItem comment = BoardItem.builder()
                    .rootId(rootId)
                    .category(BoardCategory.NOTE)
                    .content(content)
                    .writer(member)
                    .reLevel(1)
                    .reCnt(commentRepository.countByRootId(rootId) + 1)
                    .build();

            commentRepository.save(comment);
            return;
        }
        createSubComment(parentId, content, member);
    }

    @Transactional
    private void createSubComment(Long parentId, String content, UserItem member) { // 들어오는 id는 답글 달 댓글의 id
        BoardItem parentComment = commentRepository.findById(parentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );

        int myReCnt = parentComment.getReCnt();
        int myReLevel = parentComment.getReLevel();
        Long rootId = parentComment.getRootId();

        Integer index = commentRepository.findMinReCnt(rootId, myReLevel, myReCnt);

        if(index==null || index == -1 || index == 0) {
            index = commentRepository.countByRootId(rootId) + 1;
        }

        commentRepository.updateReCntForComment(rootId, index);

        BoardItem newComment = BoardItem.builder()
                .category(BoardCategory.NOTE)
                .content(content)
                .rootId(rootId)
                .reLevel(myReLevel + 1)
                .reCnt(index)
                .writer(member)
                .build();

        commentRepository.save(newComment);
    }

    @Transactional
    public Long updateCommentItem(Long id, String content) {
        BoardItem comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );
        comment.patch(content);
        commentRepository.save(comment);
        return comment.getRootId();
    }

    @Transactional
    public Long deleteCommentItem(Long id) {
        BoardItem comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );

        Long boardId = comment.getRootId();

        // decrease comment recnt, rootid/recnt가지고.
        commentRepository.decreaseReCntForComment(comment.getRootId(), comment.getReCnt());
        commentRepository.delete(comment);

        return boardId;
    }

    public BoardItem getCommentInfo(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );
    }
}
