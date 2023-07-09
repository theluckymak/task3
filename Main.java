package ru.itis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.models.Course;
import ru.itis.repositories.CoursesRepository;
import ru.itis.repositories.CoursesRepositoryJdbcImpl;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/taxi_db");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("qwerty007");
        hikariConfig.setDriverClassName("org.postgresql.Driver");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        CoursesRepository coursesRepository = new CoursesRepositoryJdbcImpl(dataSource);



        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(dataSource);

        Student student = Student.builder()
                .firstName("Имя1")
                .lastName("Фамилия1")
                .age(25)
                .build();

        System.out.println(student);
        studentsRepository.save(student);
        System.out.println(student);

        System.out.println(studentsRepository.findAll());


        // Save a new course
        Course course1 = new Course("Java", LocalDate.of(2022, 2, 2), LocalDate.of(2023, 2, 2));
        coursesRepository.save(course1);
        System.out.println("Saved course: " + course1);

        // Save another course
        Course course2 = new Course("SQL", LocalDate.of(2022, 5, 6), LocalDate.of(2023, 10, 11));
        coursesRepository.save(course2);
        System.out.println("Saved course: " + course2);

        // Update course
        course1.setTitle("Java Programming");
        course1.setFinishDate(LocalDate.of(2023, 3, 15));
        coursesRepository.update(course1);
        System.out.println("Updated course: " + course1);

        // get all courses
        List<Course> allCourses = coursesRepository.findAll();
        System.out.println("All courses:");
        for (Course course : allCourses) {
            System.out.println(course);
        }
    }
}
