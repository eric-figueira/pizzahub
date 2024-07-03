### Features

#### Authentication and Authorization:

- Login
- Signup
- Password recovery
- Roles for routing permission
  
#### Pizzeria specific:

- **Manager**:
  - It should be able to change pizzeria data
  - It should be able to hire an employee
  - It should be able to fire an employee
  - It should be able to change the position, name and email of an employee
  - It should be able to view employees
  - It should be able to generate a new password for an employee Note: All features of a manager only apply to the pizzeria that the manager is responsible for
- **Chef**:
  - It should be able to view orders with status: In production and Not started
  - It should be able to update the status of an order to Ready
- **Billing**:
  - It should be able to view all orders and filter them
  - It should be able to view the items of an order and filter them
- **Waiter**:
  - It should be able to view menu items
  - It should be able to create an order (without delivery fee)
  - It should be able to change an order only to add or remove items
  - It should be able to cancel an order
- **Cashier**:
  - It should be able to change an order only to add payment method
  - It should be able to view order data
  - It should be able to change a customer only to add CPF
When a waiter creates an order (service 1), it will create a walk-in customer (service 2).

#### Pizzeria network related:

- **Owner**:
  - It should be able to register, view, change and remove:
    - Administrators
    - Managers
- **Administrator**:
  - It should be able to register, view and filter, change and remove:
    - Pizzerias
    - Menu items
    - Ingredients
    - Ingredients of a menu item
    - Payment methods
- **Anonymous**:
  - It should be able to view and filter menu items
- **User**:
  - It should be able to change account data
  - It should be able to delete account
  - It should be able to place an order*
  - It should be able to view and filter menu items
  - It should be able to view and filter pizzerias by distance**
  - It should be able to view their orders and related data (payment method, status and menu items)
