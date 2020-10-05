package ru.otus.servlet;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceException;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;


public class UsersServlet extends HttpServlet {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONES = "phones";
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "users";

    private final Logger logger = LoggerFactory.getLogger(UsersServlet.class);
    private final DBServiceUser dbServiceUser;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DBServiceUser dbServiceUser) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        val all = dbServiceUser.getAll();
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, Map.of(TEMPLATE_ATTR_USERS, all)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        val user = createUser(req);
        if (user.getName().isEmpty())
            resp.setStatus(SC_BAD_REQUEST);
        else
            try {
                dbServiceUser.save(user);
                resp.sendRedirect("/users");
            } catch (DbServiceException e) {
                logger.error("User save error", e);
                resp.setStatus(SC_BAD_REQUEST);
            }
    }

    private User createUser(HttpServletRequest req) {
        val name = req.getParameter(PARAM_NAME);
        val address = req.getParameter(PARAM_ADDRESS);
        val phones = req.getParameter(PARAM_PHONES);
        val user = new User(name);
        if (!address.isEmpty())
            user.setAddress(new AddressDataSet(address));
        Stream.of(phones.split(","))
                .filter(Predicate.not(String::isEmpty))
                .map(PhoneDataSet::new)
                .forEach(user::addPhone);
        return user;
    }


}
