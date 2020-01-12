package com.jojoldu.book.springboot.service.posts;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// final이선언된 모든 필드를 인자 값으로 하는 생성자를 롬복의 @RequiredArgsConstructor가 대신 생성하여 스프링의 @Autowired를 사용할 필요가 없다.
// 생성자를 안 쓰고 롬복 어노테이션을 사용한 이유는 간단하다. - 해당 클래스의 의존성 관계가 변경될 때 마다 생성자 코드를 수정하는 번거로움 해결
@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // Database 쿼리를 날리는 코드가 없다.
    // 그런데 가능한 이유는 JPA의 영속성 컨텍스트 덕분이다.
    // 영속성 컨텍스트란, 엔티티를 영구 저장하는 환경이다.
    // JPA의 핵심 내용은 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐로 갈린다.
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id: " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id: " + id));

        return new PostsResponseDto(entity);
    }

}
