package ru.itis.repositories;

import ru.itis.models.Course;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursesRepositoryJdbcImpl implements CoursesRepository {

    private static final String SQL_SELECT_ALL = "SELECT * FROM course";
    private static final String SQL_INSERT = "INSERT INTO course(title, start_date, finish_date) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE course SET title = ?, start_date = ?, finish_date = ? WHERE id = ?";

    private final DataSource dataSource;

    public CoursesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Course model) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, model.getTitle());
                statement.setDate(2, Date.valueOf(model.getStartDate()));
                statement.setDate(3, Date.valueOf(model.getFinishDate()));

                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Cannot insert course");
                }

                try (ResultSet generatedIds = statement.getGeneratedKeys()) {
                    if (generatedIds.next()) {
                        model.setId(generatedIds.getInt(1));
                    } else {
                        throw new SQLException("Cannot retrieve id");
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {
                while (resultSet.next()) {
                    Course course = new Course();
                    course.setId(resultSet.getInt("id"));
                    course.setTitle(resultSet.getString("title"));
                    course.setStartDate(resultSet.getDate("start_date").toLocalDate());
                    course.setFinishDate(resultSet.getDate("finish_date").toLocalDate());

                    courses.add(course);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return courses;
    }

    @Override
    public void update(Course model) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
                statement.setString(1, model.getTitle());
                statement.setDate(2, Date.valueOf(model.getStartDate()));
                statement.setDate(3, Date.valueOf(model.getFinishDate()));
                statement.setInt(4, model.getId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Cannot update course");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
