package org.example.g14.service;

import org.example.g14.dto.response.PostResponseDto;
import org.example.g14.exception.OrderInvalidException;
import org.example.g14.model.Product;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    IUserServiceInternal iUserServiceInternal;

    @Mock
    PostRepository repository;

    @InjectMocks
    PostService service;

    private void initializeFeature0005() {
        MockitoAnnotations.openMocks(this);

        // arrange
        int sellerId = 1;
        User user = new User(2, "John Doe", List.of(), List.of(1));

        // act
        when(iUserServiceInternal.searchUserIfExists(user.getId())).thenReturn(user);
        when(repository.findAllByUser(sellerId)).thenReturn(List.of());
    }

    @Test
    @DisplayName("US-0009 Verificar que el tipo de ordenamiento por fecha exista. OK")
    public void getPostsFromFollowedTestOk(){
        initializeFeature0005();
        List<PostResponseDto> result_asc = service.getPostsFromFollowed(2, "date_asc");
        List<PostResponseDto> result_desc = service.getPostsFromFollowed(2, "date_desc");

        // assert
        Assertions.assertNotNull(result_asc, "El test por fecha ASCENDENTE finalizó correctamente");
        Assertions.assertNotNull(result_desc, "El test por fecha DESCENDENTE finalizó correctamente");
    }

    @Test
    @DisplayName("US-0009 Verificar que el tipo de ordenamiento por fecha exista. BAD REQUEST")
    public void getPostsFromFollowedTestBadRequest() {
        initializeFeature0005();
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
        when(iUserServiceInternal.searchUserIfExists(userId)).thenReturn(user);
        when(repository.findAllByUser(sellerId)).thenReturn(PostList.getPostResponse());

        List<LocalDate> obtainedDates = service.getPostsFromFollowed(userId, order).stream()
                .map(PostResponseDto::getDate)
                .collect(Collectors.toList());

        //assert
        assertEquals(expectedDates, obtainedDates);
    }

    @Test
    @DisplayName("US-0006 verificar que traigan post de las ultimas 2 semanas")
    public void testGetPostsFromFollowedWithinLastTwoWeeks() {
        // Arrange
        int userId = 1;
        String order = "date_desc";

        User user = new User();
        user.setId(userId);
        List<Integer> followedVendors = new ArrayList<>();
        followedVendors.add(2);
        user.setIdFollows(followedVendors);
        when(iUserServiceInternal.searchUserIfExists(userId)).thenReturn(user);

        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1);
        post1.setIdUser(2);
        post1.setDate(LocalDate.now().minusDays(7)); // Within last two weeks
        post1.setProduct(new Product(1,"product 1", "type 1", "brand 1", "color 1", "note 1" ));
        posts.add(post1);
        Post post2 = new Post();
        post2.setId(2);
        post2.setIdUser(2);
        post2.setDate(LocalDate.now().minusDays(15)); // Outside last two weeks
        post2.setProduct(new Product(2,"product 2", "type 2", "brand 2", "color 2", "note 2" ));
        posts.add(post2);
        when(repository.findAllByUser(2)).thenReturn(posts);

        // Act
        List<PostResponseDto> result = service.getPostsFromFollowed(userId, order);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPost_id());
    }
}
