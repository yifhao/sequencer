package com.july;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by haoyifen on 2017/5/23 11:39.
 */
@RestController
public class Controller {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @RequestMapping("/ids")
    public Map<String, Object> get(String bizTag) throws SQLException {
        TransactionTemplate tt = new TransactionTemplate(transactionManager);
        Map<String, Object> result = tt.execute(it -> {
            int update = jdbcTemplate.update("UPDATE leaf SET min_id=max_id,max_id=max_id+step WHERE biz_tag=?", bizTag);
            if (update==0) {
                throw new IllegalArgumentException("not exist biz_tag: " + bizTag);
            }
            return jdbcTemplate.queryForMap("SELECT biz_tag, min_id,max_id, step FROM leaf WHERE biz_tag=?", bizTag);
        });
        return result;
    }
}
