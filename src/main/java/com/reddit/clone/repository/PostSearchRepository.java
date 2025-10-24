//package com.reddit.clone.repository;
//
//import com.reddit.clone.entity.PostDocument;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import java.util.List;
//
//
//public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {
//
//    List<PostDocument> findByTitleContainingOrContentContaining(String title, String content);
//}
