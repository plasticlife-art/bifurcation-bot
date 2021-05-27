package com.math.bifurcation.data.coffee.recipe;

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
import java.util.*;

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

    public void store(CoffeeRecipe coffeeRecipe) {
        CoffeeRecipe byId = findById(coffeeRecipe.getId());
        if (byId == null) {
            save(coffeeRecipe);
        } else {
            update(coffeeRecipe);
        }
    }

    private void update(CoffeeRecipe recipe) {
        log.debug("CoffeeRecipeRepository.update()");
        HashMap<String, Object> params = new HashMap<>();
        params.put(COLUMN_ID, recipe.getId().toString());
        params.put(COLUMN_NAME, recipe.getName());
        params.put(COLUMN_FLOWERING_WATER_AMOUNT, recipe.getFloweringWaterAmount());
        params.put(COLUMN_FLOWERING_TIME, recipe.getFloweringTime());
        params.put(COLUMN_POUROVER_TIME, recipe.getPouroverTime());
        params.put(COLUMN_BEANS_AMOUNT, recipe.getBeansAmount());
        params.put(COLUMN_TEMP, recipe.getTemp());

        jdbcTemplate.update("UPDATE " + TABLE_NAME +
                        " SET " + COLUMN_NAME + "= :" + COLUMN_NAME + ", "
                        + COLUMN_FLOWERING_WATER_AMOUNT + "= :" + COLUMN_FLOWERING_WATER_AMOUNT + ", "
                        + COLUMN_FLOWERING_TIME + "= :" + COLUMN_FLOWERING_TIME + ", "
                        + COLUMN_POUROVER_TIME + "= :" + COLUMN_POUROVER_TIME + ", "
                        + COLUMN_BEANS_AMOUNT + "= :" + COLUMN_BEANS_AMOUNT + ", "
                        + COLUMN_TEMP + "= :" + COLUMN_TEMP + " "
                        + "WHERE " + COLUMN_ID + " =:" + COLUMN_ID,
                params);
    }

    private void save(CoffeeRecipe recipe) {
        log.debug("CoffeeRecipeRepository.save()");
        HashMap<String, Object> params = new HashMap<>();
        params.put(COLUMN_ID, recipe.getId());
        params.put(COLUMN_OWNER_USER_ID, recipe.getOwner_user_id());
        params.put(COLUMN_NAME, recipe.getName());
        params.put(COLUMN_FLOWERING_WATER_AMOUNT, recipe.getFloweringWaterAmount());
        params.put(COLUMN_FLOWERING_TIME, recipe.getFloweringTime());
        params.put(COLUMN_POUROVER_TIME, recipe.getPouroverTime());
        params.put(COLUMN_BEANS_AMOUNT, recipe.getBeansAmount());
        params.put(COLUMN_TEMP, recipe.getTemp());
        params.put(COLUMN_CREATE_TS, recipe.getCreate_ts().getTime());

        jdbcTemplate.update("INSERT INTO " + TABLE_NAME +
                        " VALUES (:" + COLUMN_ID + ", " +
                        ":" + COLUMN_OWNER_USER_ID + ", " +
                        ":" + COLUMN_NAME + ", " +
                        ":" + COLUMN_FLOWERING_WATER_AMOUNT + ", " +
                        ":" + COLUMN_FLOWERING_TIME + ", " +
                        ":" + COLUMN_POUROVER_TIME + ", " +
                        ":" + COLUMN_BEANS_AMOUNT + ", " +
                        ":" + COLUMN_TEMP + ", " +
                        ":" + COLUMN_CREATE_TS + ")",
                params);
    }

    public boolean isExist(CoffeeRecipe user) {
        log.debug("CoffeeRecipeRepository.isExist()");
        return findById(user.getId()) != null;
    }

    public CoffeeRecipe findById(UUID id) {
        log.debug("CoffeeRecipeRepository.findById()");

        if (id == null) {
            return null;
        }

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(COLUMN_ID, id.toString());

        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_ID + " = :" + COLUMN_ID,
                    namedParameters, (rs, rowNum) -> convert(rs));
        } catch (EmptyResultDataAccessException e) { //if not found
            return null;
        }
    }

    private CoffeeRecipe convert(ResultSet resultSet) throws SQLException {
        return CoffeeRecipe.builder()
                .id(UUID.fromString(resultSet.getString(COLUMN_ID)))
                .owner_user_id(resultSet.getLong(COLUMN_OWNER_USER_ID))
                .name(resultSet.getString(COLUMN_NAME))
                .floweringWaterAmount(resultSet.getInt(COLUMN_FLOWERING_WATER_AMOUNT))
                .floweringTime(resultSet.getInt(COLUMN_FLOWERING_TIME))
                .pouroverTime(resultSet.getInt(COLUMN_POUROVER_TIME))
                .beansAmount(resultSet.getInt(COLUMN_BEANS_AMOUNT))
                .temp(resultSet.getInt(COLUMN_TEMP))
                .create_ts(new Date(resultSet.getLong(COLUMN_CREATE_TS)))
                .build();
    }

    public int getCount() {
        log.debug("CoffeeRecipeRepository.getCount()");
        return findAll().size();
    }
}

