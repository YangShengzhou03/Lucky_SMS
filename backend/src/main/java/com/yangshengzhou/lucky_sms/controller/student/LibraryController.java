package com.yangshengzhou.lucky_sms.controller.student;

import com.yangshengzhou.lucky_sms.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Resource;
import java.util.*;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/student/library")
public class LibraryController {

    @Resource
    private JwtUtil jwtUtil;

    @GetMapping("/search")
    public HashMap<String, Object> searchBooks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "relevance") String sort,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            List<Map<String, Object>> books = new ArrayList<>();
            
            if (query != null && !query.isEmpty()) {
                books.add(createBookData(101, "深入理解计算机系统", "Randal E.Bryant", true, 5));
                books.add(createBookData(102, "代码整洁之道", "Robert C. Martin", false, 0));
                books.add(createBookData(103, "设计模式：可复用面向对象软件的基础", "Erich Gamma等", true, 2));
                books.add(createBookData(104, "算法导论", "Thomas H. Cormen", true, 3));
                books.add(createBookData(105, "计算机程序的构造和解释", "Harold Abelson", true, 1));
            }
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, books.size());
            List<Map<String, Object>> pageBooks = books.subList(start, end);
            
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("records", pageBooks);
            pageData.put("total", books.size());
            pageData.put("size", size);
            pageData.put("current", page);
            pageData.put("pages", (int) Math.ceil((double) books.size() / size));
            
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", pageData);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/detail/{bookId}")
    public HashMap<String, Object> getBookDetail(
            @PathVariable Integer bookId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            Map<String, Object> book = createBookData(bookId, "图书标题", "作者", true, 3);
            book.put("publisher", "出版社名称");
            book.put("year", "2023");
            book.put("isbn", "9787111544937");
            book.put("pages", "737");
            book.put("callNumber", "TP311.5/B915");
            book.put("location", "三楼自然科学区");
            book.put("description", "这是一本优秀的图书，内容丰富，适合深入学习相关知识。");
            book.put("type", "book");
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", book);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @PostMapping("/reserve")
    public HashMap<String, Object> reserveBook(
            @RequestParam Integer bookId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            result.put("code", 200);
            result.put("message", "预约成功，请在3天内到图书馆借阅");
            result.put("data", null);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    @GetMapping("/borrows")
    public HashMap<String, Object> getMyBorrows(
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            List<Map<String, Object>> borrows = new ArrayList<>();
            borrows.add(createBorrowData(1, "深入理解计算机系统", "2023-06-15", "2023-07-15", 0));
            borrows.add(createBorrowData(2, "代码整洁之道", "2023-06-20", "2023-07-20", 1));
            borrows.add(createBorrowData(3, "设计模式", "2023-07-01", "2023-07-31", 0));
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", borrows);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    /**
     * 续借图书
     * @param borrowId 借阅记录ID
     * @param request HTTP请求
     * @return 续借结果
     */
    @PostMapping("/renew")
    public HashMap<String, Object> renewBook(
            @RequestParam Integer borrowId,
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            result.put("code", 200);
            result.put("message", "续借成功，新的应还日期为30天后");
            result.put("data", null);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    /**
     * 获取热门推荐图书
     * @param request HTTP请求
     * @return 热门推荐图书列表
     */
    @GetMapping("/recommended")
    public HashMap<String, Object> getRecommendedBooks(
            HttpServletRequest request
    ) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            Integer userId = jwtUtil.getUidByRequest(request);
            
            List<Map<String, Object>> recommended = new ArrayList<>();
            recommended.add(createRecommendedBook(101, "深入理解计算机系统", true));
            recommended.add(createRecommendedBook(102, "代码整洁之道", false));
            recommended.add(createRecommendedBook(103, "设计模式", false));
            recommended.add(createRecommendedBook(104, "算法导论", true));
            recommended.add(createRecommendedBook(105, "计算机程序的构造和解释", false));
            
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", recommended);
        } catch (ResponseStatusException e) {
            result.put("code", e.getStatusCode().value());
            result.put("message", e.getReason());
            result.put("data", null);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
            result.put("data", null);
        }

        return result;
    }

    private Map<String, Object> createBookData(int id, String title, String author, boolean available, int availableCopies) {
        Map<String, Object> book = new HashMap<>();
        book.put("id", id);
        book.put("title", title);
        book.put("author", author);
        book.put("available", available);
        book.put("availableCopies", availableCopies);
        book.put("cover", "https://picsum.photos/200/300?random=" + id);
        if (!available) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 15);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            book.put("returnDate", sdf.format(calendar.getTime()));
        }
        return book;
    }

    private Map<String, Object> createBorrowData(int id, String title, String borrowDate, String dueDate, int renewTimes) {
        Map<String, Object> borrow = new HashMap<>();
        borrow.put("id", id);
        borrow.put("title", title);
        borrow.put("borrowDate", borrowDate);
        borrow.put("dueDate", dueDate);
        borrow.put("renewTimes", renewTimes);
        borrow.put("cover", "https://picsum.photos/200/300?random=" + id);
        return borrow;
    }

    private Map<String, Object> createRecommendedBook(int id, String title, boolean isNew) {
        Map<String, Object> book = new HashMap<>();
        book.put("id", id);
        book.put("title", title);
        book.put("isNew", isNew);
        book.put("cover", "https://picsum.photos/200/300?random=" + id);
        return book;
    }
}