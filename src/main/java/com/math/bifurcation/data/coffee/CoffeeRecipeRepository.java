package com.math.bifurcation.data.coffee;

import com.math.bifurcation.telegram.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Leonid Cheremshantsev
 */
@Service
public class CoffeeRecipeRepository {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OWNER_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FLOWERING_WATER_AMOUNT = "floweringWaterAmount";
    public static final String COLUMN_FLOWERING_TIME = "floweringTime";
    public static final String COLUMN_POUROVER_TIME = "pouroverTime";
    public static final String COLUMN_BEANS_AMOUNT = "beansAmount";
    public static final String COLUMN_TEMP = "temp";
    public static final String COLUMN_CREATE_TS = "create_ts";
    public static final String TABLE_NAME = "recipes";

    private final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CoffeeRecipeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s VARCHAR(100), " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER, " +
                        "%s INTEGER)",
                TABLE_NAME,
                COLUMN_ID,
                COLUMN_OWNER_USER_ID,
                COLUMN_NAME,
                COLUMN_FLOWERING_WATER_AMOUNT,
                COLUMN_FLOWERING_TIME,
                COLUMN_POUROVER_TIME,
                COLUMN_BEANS_AMOUNT,
                COLUMN_TEMP,
                COLUMN_CREATE_TS);

        jdbcTemplate.getJdbcTemplate()
                .execute(sql);
    }

    public Set<CoffeeRecipe> findAll() {
        log.debug("CoffeeRecipeRepository.findAll()");
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM " + TABLE_NAME,
                (resultSet, rowNum) -> convert(resultSet)));
    }

    private CoffeeRecipe convert(ResultSet resultSet) throws SQLException {
        return CoffeeRecipe.builder()
                .id(resultSet.getLong(COLUMN_ID))
                .name(resultSet.getString(COLUMN_NAME))
                .floweringWaterAmount(resultSet.getInt(COLUMN_FLOWERING_WATER_AMOUNT))
                .floweringTime(resultSet.getInt(COLUMN_FLOWERING_TIME))
                .pouroverTime(resultSet.getInt(COLUMN_POUROVER_TIME))
                .beansAmount(resultSet.getInt(COLUMN_BEANS_AMOUNT))
                .temp(resultSet.getInt(COLUMN_TEMP))
                .create_ts(new Date(resultSet.getLong(COLUMN_CREATE_TS)))
                .build();
    }
}

