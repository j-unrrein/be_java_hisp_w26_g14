package org.example.g14.service;

import org.example.g14.dto.response.PostResponseDto;
import org.example.g14.model.Post;
import org.example.g14.model.User;
import org.example.g14.repository.PostRepository;
import org.example.g14.utils.PostList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    IUserServiceInternal iUserServiceInternal;
    @Mock
    PostRepository repository;
    @InjectMocks
    PostService service;

    @Test
    @DisplayName("US-0009 - ordenamiento descendente correcto")
    public void getPostsFromFollowedDescOk(){
        //arrange
        String order = "date_desc";
        List<LocalDate> dateDescExpected = PostList.getPostResponse().stream()
                .map(Post::getDate)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        //act y assert
        getPostsFromFollowedOk(order, dateDescExpected);
    }

    @Test
    @DisplayName("US-0009 - ordenamiento ascendente correcto")
    public void getPostsFromFollowedAscOk(){
        //arrange
        String order = "date_asc";
        List<LocalDate> dateAscExpected = PostList.getPostResponse().stream()
                .map(Post::getDate)
                .sorted()
                .collect(Collectors.toList());
        //act y assert
        getPostsFromFollowedOk(order, dateAscExpected);
    }

    private void getPostsFromFollowedOk(String order, List<LocalDate> expectedDates){
        //arrange
        int sellerId= 1;
        int userId = 2;

        User user = new User(userId,"Pedro", new ArrayList<>(), List.of(1));

        //act
        PostList ResponseDtoList;
        Mockito.when(iUserServiceInternal.searchUserIfExists(userId)).thenReturn(user);
        Mockito.when(repository.findAllByUser(sellerId)).thenReturn(PostList.getPostResponse());

        List<LocalDate> obtainedDates = service.getPostsFromFollowed(userId, order).stream()
                .map(PostResponseDto::getDate)
                .collect(Collectors.toList());

        //assert
        Assertions.assertEquals(expectedDates, obtainedDates);
    }
}
