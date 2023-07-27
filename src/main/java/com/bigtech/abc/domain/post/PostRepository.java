//package com.bigtech.abc.post;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Repository;
//
//import java.awt.print.Pageable;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static com.bigtech.abc.conntection.DBConnectionUtil.getConnection;
//
//@Slf4j
//@Repository
//public class PostRepository {
//    public Post save(Post post) throws SQLException {
//        String sql = "insert into post (id, content, likes, createdDate, modifiedDAte) values (?, ?, ?, ?, ?)";
//
//        Connection con = null;
//        PreparedStatement pstmt = null; // injection 공격을 예방하기 위해 바인딩 방식 사용
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setLong(1, post.getId());
//            pstmt.setString(2, post.getContent());
//            pstmt.setInt(3, post.getLikes());
//            pstmt.setTimestamp(4, post.getCreatedDate());
//            pstmt.setTimestamp(5, post.getModifiedDate());
//            pstmt.executeUpdate();
//            return post;
//
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally { // 커넥션을 닫아줘야함
//            close(con, pstmt, null);
//        }
//    }
//
//    public void saveAll(List<Post> postList) throws SQLException {
//        String sql = "insert into post (id, content, likes, createdDate, modifiedDAte) values (?, ?, ?, ?, ?)";
//
//        Connection con = null;
//        PreparedStatement pstmt = null; // injection 공격을 예방하기 위해 바인딩 방식 사용
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//
//            for (Post post: postList) {
//                pstmt.setLong(1, post.getId());
//                pstmt.setString(2, post.getContent());
//                pstmt.setInt(3, post.getLikes());
//                pstmt.setTimestamp(4, post.getCreatedDate());
//                pstmt.setTimestamp(5, post.getModifiedDate());
//                pstmt.addBatch();
//            }
//            pstmt.executeBatch();
//
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally { // 커넥션을 닫아줘야함
//            close(con, pstmt, null);
//        }
//    }
//
//
//
//    public Post findById(Long id) throws SQLException {
//        String sql = "select * from post where id = ?";
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setLong(1, id);
//
//            rs = pstmt.executeQuery();
//            if (rs.next()) {
//                Post post = new Post();
//                post.setId(rs.getLong("id"));
//                post.setContent(rs.getString("content"));
//                post.setLikes(rs.getInt("likes"));
//                post.setCreatedDate(rs.getTimestamp("createdDate"));
//                post.setModifiedDate(rs.getTimestamp("modifiedDate"));
//                return post;
//            } else {
//                throw new NoSuchElementException("post not found id" + id);
//            }
//
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally {
//            close(con, pstmt, rs);
//        }
//    }
//
//    public List<Post> findAll() throws SQLException {
//        String sql = "select * from post";
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//            List<Post> postList= new ArrayList<>();
//
//            while(rs.next()) {
//                Post post = new Post();
//                post.setId(rs.getLong("id"));
//                post.setContent(rs.getString("content"));
//                post.setLikes(rs.getInt("likes"));
//                post.setCreatedDate(rs.getTimestamp("createdDate"));
//                post.setModifiedDate(rs.getTimestamp("modifiedDate"));
//                postList.add(post);
//                log.info("id : {}, content : {}, likes : {}, createdDate : {}, modifiedDate : {}", post.getId(), post.getContent(), post.getLikes(), post.getCreatedDate(), post.getModifiedDate());
//
////                if (!rs.next()) {
////                        throw new NoSuchElementException("post not found");
////                }
//            }
//            return postList;
//
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally {
//            close(con, pstmt, rs);
//        }
//    }
//
//    public List<Post> findPostByPage(String sql) throws SQLException {
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//            List<Post> postList= new ArrayList<>();
//
//            while(rs.next()) {
//                Post post = new Post();
//                post.setId(rs.getLong("id"));
//                post.setContent(rs.getString("content"));
//                post.setLikes(rs.getInt("likes"));
//                post.setCreatedDate(rs.getTimestamp("createdDate"));
//                post.setModifiedDate(rs.getTimestamp("modifiedDate"));
//                postList.add(post);
//                log.info("id : {}, content : {}, likes : {}, createdDate : {}, modifiedDate : {}", post.getId(), post.getContent(), post.getLikes(), post.getCreatedDate(), post.getModifiedDate());
//            }
//            return postList;
//
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally {
//            close(con, pstmt, rs);
//        }
//    }
//
//    public List<Post> getPostByPage(int pageNumber, int pageSize) throws SQLException {
//        int startIndex = (pageNumber - 1) * pageSize;
//        String query = "SELECT * FROM post LIMIT " + startIndex + ", " + pageSize;
//
//        List<Post> postList = findPostByPage(query);
//
//        return postList;
//    }
//
//
//
//    public void update(Long id, String content) throws SQLException {
//        String sql = "update post set content=? where id=?";
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setString(1, content);
//            pstmt.setLong(2, id);
//            int resultSize = pstmt.executeUpdate();
//            log.info("ResultSize = {}", resultSize);
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally { // 커넥션을 닫아줘야함
//            close(con, pstmt, null);
//        }
//    }
//
//    public void delete(Long id) throws SQLException {
//        String sql = "delete from post where id=?";
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(sql);
//            pstmt.setLong(1, id);;
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            log.error("db error", e);
//            throw e;
//        } finally { // 커넥션을 닫아줘야함
//            close(con, pstmt, null);
//        }
//    }
//
//
//    private void close(Connection con, Statement stmt, ResultSet rs) {
//        if (rs != null) {
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                log.info("error", e);
//            }
//        }
//
//        if (stmt != null) {
//            try {
//                stmt.close();
//            } catch (SQLException e) {
//                log.info("error", e);
//            }
//
//        }
//
//        if (con != null) {
//            try {
//                con.close();
//            } catch (SQLException e) {
//                log.info("error", e);
//            }
//        }
//
//
//
//    }
//}
