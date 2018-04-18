package org.superbiz.moviefun.albums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static final long SECONDS = 1000;

    private final JdbcTemplate jdbcTemplate;
    private final AlbumsUpdater albumsUpdater;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AlbumsUpdateScheduler(DataSource dataSource, AlbumsUpdater albumsUpdater) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.albumsUpdater = albumsUpdater;
    }


    @Scheduled(initialDelay = 5 * SECONDS, fixedRate = 15 * SECONDS)
    public void run() {
        try {
            logger.debug("Checking for albums task to start");

            if (startAlbumSchedulerTask()) {
                logger.debug("Starting albums update");

                albumsUpdater.update();

                logger.debug("Finished albums update");
            } else {
                logger.debug("Nothing to start");
            }

        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
        }
    }

    private boolean startAlbumSchedulerTask() {
        logger.debug("Create Table if not exist");
        jdbcTemplate.execute("CREATE TABLE if not exists album_scheduler_task (started_at TIMESTAMP NULL DEFAULT NULL);");
        logger.debug("Created");

        int updatedRows = jdbcTemplate.update(
                "UPDATE album_scheduler_task" +
                        " SET started_at = now()" +
                        " WHERE started_at IS NULL" +
                        " OR started_at < date_sub(now(), INTERVAL 2 MINUTE)"
        );
        String sql = "SELECT COUNT(*) FROM album_scheduler_task";
        int row = jdbcTemplate.queryForObject(
                sql, Integer.class);

        if (row<=0){
            logger.debug("Trying to insert row");
            jdbcTemplate.execute("INSERT INTO album_scheduler_task (started_at) VALUES (NULL);");
            logger.debug("inserted");
        }
        return updatedRows > 0;
    }
}
