CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    area DOUBLE PRECISION NOT NULL,

    north_wall_area DOUBLE,
    north_wall_windows INT,

    west_wall_area DOUBLE,
    west_wall_windows INT,

    south_wall_area DOUBLE,
    south_wall_windows INT,

    east_wall_area DOUBLE,
    east_wall_windows INT
);

CREATE TABLE weather (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    avg_summer_temp DOUBLE PRECISION NOT NULL,
    avg_winter_temp DOUBLE PRECISION NOT NULL,
    wet_bulb_temp DOUBLE PRECISION NOT NULL,
    dry_bulb_temp DOUBLE PRECISION NOT NULL
);