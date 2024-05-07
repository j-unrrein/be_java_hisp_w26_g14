package org.example.g14.service;

import org.example.g14.dto.response.PostResponseDto;
import org.example.g14.exception.OrderInvalidException;
import org.example.g14.model.User;
import org.example.g14.repository.PostRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.example.g14.model.Post;
import org.example.g14.utils.PostList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // arrange
        int sellerId = 1;
        User user = new User(2, "John Doe", List.of(), List.of(1));

        // act
        Mockito.when(iUserServiceInternal.searchUserIfExists(user.getId())).thenReturn(user);
        Mockito.when(repository.findAllByUser(sellerId)).thenReturn(List.of());
    }

    @Test
    @DisplayName("US-0009 Verificar que el tipo de ordenamiento por fecha exista. OK")
    public void getPostsFromFollowedTestOk(){
        List<PostResponseDto> result_asc = service.getPostsFromFollowed(2, "date_asc");
        List<PostResponseDto> result_desc = service.getPostsFromFollowed(2, "date_desc");

        // assert
        Assertions.assertNotNull(result_asc, "El test por fecha ASCENDENTE finalizó correctamente");
        Assertions.assertNotNull(result_desc, "El test por fecha DESCENDENTE finalizó correctamente");
    }

    @Test
    @DisplayName("US-0009 Verificar que el tipo de ordenamiento por fecha exista. BAD REQUEST")
    public void getPostsFromFollowedTestBadRequest() {
        // assert
        Assertions.assertThrows(OrderInvalidException.class, () -> {
            service.getPostsFromFollowed(2, "asc");
        });

        Assertions.assertThrows(OrderInvalidException.class, () -> {
            service.getPostsFromFollowed(2, "desc");
        });
    }

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
        Mockito.when(iUserServiceInternal.searchUserIfExists(userId)).thenReturn(user);
        Mockito.when(repository.findAllByUser(sellerId)).thenReturn(PostList.getPostResponse());

        List<LocalDate> obtainedDates = service.getPostsFromFollowed(userId, order).stream()
                .map(PostResponseDto::getDate)
                .collect(Collectors.toList());

        //assert
        Assertions.assertEquals(expectedDates, obtainedDates);
    }
}
