package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 8/22/21.
 */
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, title, isbn, publisher from author\n" +
                " left join book on book.author_id=author.id where author.id = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(), id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        String sql = "select author.id as id, first_name, last_name, book.id as book_id, title, isbn, publisher from author\n" +
                " left join book on book.author_id=author.id where first_name = ? and last_name = ?";

        return jdbcTemplate.query(sql, new AuthorExtractor(),firstName, lastName);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                                author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {

        jdbcTemplate.update("UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(), author.getLastName(), author.getId());

        return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

}












