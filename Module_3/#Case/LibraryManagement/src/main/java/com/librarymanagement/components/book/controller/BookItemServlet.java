package com.librarymanagement.components.book.controller;

import com.librarymanagement.components.book.model.Book;
import com.librarymanagement.components.book.model.BookItem;
import com.librarymanagement.components.book.service.BookFormatDAO;
import com.librarymanagement.components.book.service.BookItemDAO;
import com.librarymanagement.components.book.service.IBookFormatDAO;
import com.librarymanagement.components.book.service.IBookItemDAO;
import com.librarymanagement.components.user.services.BookTransactionDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

@WebServlet(name = "BookItemServlet", value = "/book_item")
public class BookItemServlet extends HttpServlet {
    private BookTransactionDAO bookTransactionDAO;
    private IBookItemDAO bookItemDAO;
    private IBookFormatDAO bookFormatDAO;

    @Override
    public void init() throws ServletException {
        bookTransactionDAO = new BookTransactionDAO();
        bookItemDAO = new BookItemDAO();
        bookFormatDAO = new BookFormatDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        ServletContext context = request.getServletContext();
        context.setAttribute("bookFormats", bookFormatDAO.getAll());
        switch (action) {
            default -> showAllBookItems(request, response);
        }
    }

    private void showAllBookItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, BookItem> all = bookItemDAO.getAll();
        request.setAttribute("bookItems", all);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/table/all.jsp");
        request.setAttribute("view", "bookItem");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        switch (action) {
            case "add" -> addNewBookItem(request, response);
        }
    }

    private void addNewBookItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
        BookItem bookItem = validateBookItemDetails(request, errors);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/form/add.jsp");
        if (errors.isEmpty()) {
            Book book = (Book) request.getAttribute("book");
            boolean status = false;
            try {
                status = bookTransactionDAO.addNewBookTransaction(book, bookItem);
            } catch (SQLException e) {
                errors.put("L???i database", e.getMessage());
            }
            if (status) {
                request.setAttribute("success", true);
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("bookItem", bookItem);
                dispatcher.forward(request, response);
            }
        } else {
            request.setAttribute("bookItem", bookItem);
            dispatcher.forward(request, response);
        }
    }

    private BookItem validateBookItemDetails(HttpServletRequest request, Map<String, String> errors) {
        BookItem bookItem;
        String idStr = request.getParameter("id");
        long id = 0;
        if (idStr == null) id = System.currentTimeMillis() / 1000;
        else try {
            id = Long.parseLong(idStr);
        } catch (Exception e) {
            errors.put("ID sai ?????nh d???ng", "ID kh??ng ????ng ?????nh d???ng");
        }
        Date publishedDate = null;
        try {
            String bItemPublishedDateStr = request.getParameter("bItemPublishedDate");
            publishedDate = Date.valueOf(bItemPublishedDateStr);
        } catch (Exception e) {
            errors.put("Ng??y sai ?????nh d???ng", "Ng??y kh??ng ????ng ?????nh d???ng");
        }
        int format = 1;
        try {
            format = Integer.parseInt(request.getParameter("bItemFormat"));
        } catch (Exception e) {
            errors.put("?????nh d???ng s??ch", "?????nh d???ng s??ch kh??ng ????ng");
        }
        String publisher = request.getParameter("bItemPublisher");

        int quantity = 0;
        try {
            quantity = Integer.parseInt(request.getParameter("bItemQuantity"));
            if (quantity > 999) errors.put("s??? l?????ng", "S??? l?????ng qu?? l???n, vui l??ng th??? l???i");
        } catch (Exception e) {
            errors.put("S??? l?????ng", "S??? l?????ng sai ?????nh d???ng");
        }

        int numberOfPages = 0;
        try {
            numberOfPages = Integer.parseInt(request.getParameter("bItemPages"));
            if (numberOfPages > 21450) errors.put("s??? trang", "S??? trang qu?? l???n, vui l??ng nh???p l???i");
        } catch (Exception e) {
            errors.put("S??? trang", "S??? trang sai ?????nh d???ng");
        }
        double price = 0;
        try {
            price = Double.parseDouble(request.getParameter("bItemPrice"));
            if (price > 100000) errors.put("gi?? m?????n", "Gi?? m?????n s??ch qu?? l???n, vui l??ng nh???p l???i");
        } catch (Exception e) {
            errors.put("Gi??", "Sai ?????nh d???ng gi??");
        }
        long bookID = (Long) request.getAttribute("bookId");
        String dateAddedStr = request.getParameter("dateAdded");
        Instant dateAdded = Instant.now();
        if (dateAddedStr != null) try {
            dateAdded = Instant.parse(dateAddedStr);
        } catch (Exception e) {
            errors.put("Ng??y th??m", "Ng??y th??m sai ?????nh d???ng");
        }
        Instant dateModified = Instant.now();
        boolean isAvailable = true;
        try {
            isAvailable = Boolean.parseBoolean(request.getParameter("bIAvailable"));
        } catch (Exception e) {
            errors.put("T??nh tr???ng", "Tr??nh tr???ng kh??ng ????ng ?????nh d???ng");
        }
        boolean deleted = false;
        bookItem = new BookItem(id, publishedDate, format, publisher, numberOfPages, price, bookID, quantity, dateAdded, dateModified, isAvailable, deleted);
        return bookItem;
    }
}
