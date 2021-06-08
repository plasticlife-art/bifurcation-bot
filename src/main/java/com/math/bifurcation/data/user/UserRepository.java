package com.math.bifurcation.data.user;

import com.math.bifurcation.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Leonid Cheremshantsev
 */

@Service
public class UserRepository {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_CREATE_TS = "create_ts";
    public static final String TABLE_NAME = "users";

    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER, %s VARCHAR(100), %s INTEGER)",
                TABLE_NAME,
                COLUMN_ID,
                COLUMN_USERNAME,
                COLUMN_CREATE_TS);

        jdbcTemplate.getJdbcTemplate()
                .execute(sql);
    }

    public Set<User> findAll() {
        log.debug("UserRepository.findAll()");
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM " + TABLE_NAME + " order by " + COLUMN_CREATE_TS,
                (resultSet, rowNum) -> convert(resultSet)));
    }

    private User convert(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong(COLUMN_ID))
                .username(resultSet.getString(COLUMN_USERNAME))
                .create_ts(new Date(resultSet.getLong(COLUMN_CREATE_TS)))
                .build();
    }

    public void add(User user) {
        if (isExist(user)) return;

        store(user);
    }

    private void store(User user) {
        log.debug("UserRepository.store()");
        HashMap<String, Object> params = new HashMap<>();
        params.put(COLUMN_ID, user.getId());
        params.put(COLUMN_USERNAME, user.getUsername());
        params.put(COLUMN_CREATE_TS, user.getCreate_ts().getTime());

        jdbcTemplate.update("INSERT INTO " + TABLE_NAME +
                        " VALUES (:" + COLUMN_ID + ", " +
                        ":" + COLUMN_USERNAME + ", " +
                        ":" + COLUMN_CREATE_TS + ")",
                params);
    }

    public boolean isExist(User user) {
        log.debug("UserRepository.isExist()");
        return findById(user.getId()) != null;
    }

    public User findById(Long id) {
        log.debug("UserRepository.findById()");
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_ID, id);

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_ID + " = :" + COLUMN_ID,
                    namedParameters, (rs, rowNum) -> convert(rs));
        } catch (EmptyResultDataAccessException e) { //if not found
            return null;
        }
    }

    public int getCount() {
        log.debug("UserRepository.getCount()");
        return findAll().size();
    }
}
