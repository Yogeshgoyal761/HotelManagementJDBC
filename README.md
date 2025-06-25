# HotelManagementJDBC
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Management System - JDBC/Java/MySQL</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        h1, h2, h3 {
            color: #2c3e50;
        }
        h1 {
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
        }
        code {
            background: #f4f4f4;
            padding: 2px 5px;
            border-radius: 3px;
            font-family: 'Courier New', Courier, monospace;
        }
        pre {
            background: #f4f4f4;
            padding: 10px;
            border-radius: 5px;
            overflow-x: auto;
        }
        .badge {
            display: inline-block;
            padding: 3px 7px;
            background: #3498db;
            color: white;
            border-radius: 3px;
            font-size: 0.8em;
            margin-right: 5px;
        }
        .screenshot {
            max-width: 100%;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
    <h1>🏨 Hotel Management System</h1>
    <p>A Java application with MySQL database integration using JDBC for managing hotel operations</p>

    <div>
        <span class="badge">Java</span>
        <span class="badge">JDBC</span>
        <span class="badge">MySQL</span>
        <span class="badge">OOP</span>
    </div>

    <h2>✨ Features</h2>
    <ul>
        <li>🔍 <strong>Room Management</strong>: View available/occupied rooms</li>
        <li>🏷️ <strong>Guest Check-in/Check-out</strong>: With persistent database storage</li>
        <li>🛡️ <strong>Secure Database Operations</strong>: Using Prepared Statements</li>
        <li>♻️ <strong>Transaction Support</strong>: For data integrity</li>
        <li>📊 <strong>Console-based Interface</strong>: Easy-to-use menu system</li>
    </ul>

    <h2>📦 Prerequisites</h2>
    <ul>
        <li>Java JDK 8+</li>
        <li>MySQL Server 5.7+</li>
        <li>MySQL Connector/J (JDBC Driver)</li>
    </ul>

    <h2>🚀 Installation</h2>
    <ol>
        <li>Clone the repository:
            <pre><code>git clone https://github.com/yourusername/hotel-management-jdbc.git</code></pre>
        </li>
        <li>Import the database schema:
            <pre><code>mysql -u username -p hotel_db < schema.sql</code></pre>
        </li>
        <li>Update database credentials in <code>DatabaseConfig.java</code></li>
        <li>Compile and run:
            <pre><code>javac -cp mysql-connector-java.jar:. HotelManagementSystem.java
java -cp mysql-connector-java.jar:. HotelManagementSystem</code></pre>
        </li>
    </ol>

    <h2>📷 Screenshots</h2>
    <img src="screenshots/main-menu.png" alt="Main Menu" class="screenshot">
    <img src="screenshots/checkin-process.png" alt="Check-in Process" class="screenshot">

    <h2>🛠️ Database Schema</h2>
    <pre>
CREATE TABLE rooms (
    room_number INT PRIMARY KEY,
    guest_name VARCHAR(100),
    occupied BOOLEAN DEFAULT false,
    check_in_date DATETIME,
    check_out_date DATETIME
);
    </pre>

    <h2>🤝 Contributing</h2>
    <p>Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.</p>

    <h2>📜 License</h2>
    <p>MIT © Your Name</p>

    <h2>🙏 Acknowledgments</h2>
    <ul>
        <li>Naman Agrawal for mentorship</li>
        <li>Oracle JDBC documentation</li>
        <li>MySQL reference manuals</li>
    </ul>
</body>
</html>
