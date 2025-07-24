CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    area DOUBLE,

    north_wall_area DOUBLE,
    north_wall_windows INT,

    west_wall_area DOUBLE,
    west_wall_windows INT,

    south_wall_area DOUBLE,
    south_wall_windows INT,

    east_wall_area DOUBLE,
    east_wall_windows INT
);
