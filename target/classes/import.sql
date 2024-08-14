insert into customer (fullname, cpf) values ('Eliza Palmer', '106.101.244-23');
insert into customer (fullname, cpf) values ('Lester Walsh', '164.219.243-24');
insert into customer (fullname, cpf) values ('Lelia Yates', '201.116.237-91');
insert into customer (fullname, cpf) values ('Elizabeth Abbott', '200.46.95-39');

insert into customer (fullname, email, password, cep, house_number, house_complement) values ('Clyde Stanley', 'mavku@mojje.eg', 'JtdRWqT129', '35505-531', '23', 'House');
insert into customer (fullname, email, password, cep, house_number, house_complement) values ('Edwin Edwards', 'zemzaf@ahu.ck', 'LHNG46IzsCg500P', '75223-713', '120A', 'Apartment');
insert into customer (fullname, email, password, cep, house_number, house_complement) values ('Dean Glover', 'pik@ruce.kz', 'vHw7jL', '43939-394', '863', 'House');
insert into customer (fullname, email, password, cep, house_number, house_complement) values ('James Sullivan', 'va@zatpuspof.jo', 'uQPwkSqMzXID773R9U', '83285-762', '15B', 'Apartment');

insert into menu_item (price, slug, name) values (35.0, 'margherita', 'Margherita')
insert into menu_item (price, slug, name) values (40.0, 'calabresa', 'Calabresa')
insert into menu_item (price, slug, name) values (45.0, 'champignon', 'Champignon')
insert into menu_item (price, slug, name) values (50.0, 'brasileira', 'Brasileira')
insert into menu_item (price, slug, name) values (55.0, 'camarao', 'Camarão')
insert into menu_item (price, slug, name) values (60.0, 'strogonoff-de-carne', 'Strogonoff de carne')
insert into menu_item (price, slug, name) values (40.0, 'pepperoni', 'Pepperoni')
insert into menu_item (price, slug, name) values (45.0, 'quatro-queijos', 'Quatro queijos')
insert into menu_item (price, slug, name) values (50.0, 'frango-com-catupiry', 'Frango com catupiry')

insert into ingredient (slug, name) values ('mucarela', 'Muçarela')
insert into ingredient (slug, name) values ('tomate', 'Tomate')
insert into ingredient (slug, name) values ('manjericao', 'Manjericão')
insert into ingredient (slug, name) values ('linguica-calabresa', 'Linguiça calabresa')
insert into ingredient (slug, name) values ('cebola', 'Cebola')
insert into ingredient (slug, name) values ('champignon', 'Champignon')
insert into ingredient (slug, name) values ('oregano', 'Orégano')
insert into ingredient (slug, name) values ('ervilha', 'Ervilha')
insert into ingredient (slug, name) values ('milho', 'Milho')
insert into ingredient (slug, name) values ('palmito', 'Palmito')
insert into ingredient (slug, name) values ('camarao', 'Camarão')
insert into ingredient (slug, name) values ('catupiry', 'Catupiry')
insert into ingredient (slug, name) values ('strogonoff-de-carne', 'Strogonoff de carne')
insert into ingredient (slug, name) values ('batata-palha', 'Batata palha')

insert into menu_item_ingredients (menu_item_id, ingredient_id) values (1, 1)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (1, 2)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (1, 3)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (2, 1)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (2, 4)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (2, 5)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (3, 1)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (3, 6)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (3, 7)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (4, 1)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (4, 8)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (4, 9)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (4, 10)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (4, 2)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (5, 11)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (5, 12)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (5, 1)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (6, 13)
insert into menu_item_ingredients (menu_item_id, ingredient_id) values (6, 14)

insert into pizzeria (code, first_contact, second_contact, email, cep, address_number, created_at) values (100, '(19)3135-5235', '(19)3034-9923', 'lonzo@zewoh.gb', '13059-042', 104, '2024-10-01')

insert into worker (id, full_name, email, password, created_at, role, pizzeria_code) values ('81881bab-dac9-49a4-945c-63f6a96d52fc', 'Catherine Osborne', 'demivejec@jusnu.gov', 'Z9uanWj8gXRhxV', '2021-10-01','ANONYMOUS', 1)

insert into "order" (number, customer_id, order_date, order_time, shipping_tax, payment_method, order_status) values (123, 1, '2024-06-03', '14:30:00', 10.50, 'CREDIT_CARD', 'IN_PROGRESS')