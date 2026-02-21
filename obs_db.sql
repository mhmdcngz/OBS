CREATE TABLE IF NOT EXISTS skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    proficiency INT DEFAULT 0, -- 0 ile 100 arası yetenek seviyesi
    icon VARCHAR(50) DEFAULT 'code', -- Lucide ikon adı (örn: database, layout)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);