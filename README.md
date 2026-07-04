# GR 6: Canteen Sales and Inventory System

Java Swing + MySQL/XAMPP desktop system ready to open in Visual Studio Code.

## Included Functionalities

### Admin / Staff Portal
- Login and role-based access
- Dashboard counters for orders, payments, stock alerts, expiry alerts and sales
- POS walk-in cash sale with automatic recipe ingredient deduction
- Customer online order confirmation, paid-order preparation, ready and completed statuses
- Payment verification and rejection
- Menu categories and menu-product CRUD/search
- Ingredient inventory, recipe, supplier and batch CRUD/search
- Receive stock with inventory movement record
- Purchase orders and purchase items
- Inventory adjustments and stock movement history
- Promotions, expenses, feedback, announcements, messages, notifications and audit logs
- Reports and CSV export

### Customer/User Portal
- Register account, login and update profile/password
- Browse menu and build a multi-item pickup order cart
- View and cancel eligible orders
- Submit GCash/Maya/bank/e-wallet/cash payment reference for verification
- View announcements and notifications
- Send support messages
- Submit customer feedback

## Database Setup
1. Start MySQL in XAMPP.
2. Open phpMyAdmin.
3. Import `database/canteen_sales_inventory_system.sql`.
4. Default connection is `root` with blank password; edit `config/db.properties` when needed.

## JDBC Driver
Run `download_mysql_connector_driver_windows.bat` once to download the required `mysql-connector-j-8.4.0.jar` into the `lib` folder before database login.

## Run in VS Code / Windows
- Open this entire extracted folder in VS Code.
- Run `run_windows.bat`, or run `src/com/csis/Main.java`.

## Demo Accounts
- Admin: `admin` / `Admin@123`
- Customer: `customer1` / `User@123`
