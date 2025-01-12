SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables to reset data
TRUNCATE TABLE `pay_my_buddy`.`connections`;
TRUNCATE TABLE `pay_my_buddy`.`transaction`;
TRUNCATE TABLE `pay_my_buddy`.`account`;
TRUNCATE TABLE `pay_my_buddy`.`users`;

-- Insert data into User table
INSERT INTO `pay_my_buddy`.`users` (`id`, `username`, `email`, `password`) VALUES
                                                                              (1, 'johndoee', 'johndoe@example.com', 'password123'),
                                                                              (2, 'janedoe', 'janedoe@example.com', 'password456'),
                                                                              (3, 'bobsmith', 'bobsmith@example.com', 'password789');

-- Insert data into Account table
INSERT INTO `pay_my_buddy`.`account` (`id`, `user_id`) VALUES
                                                           (1, 1),
                                                           (2, 2),
                                                           (3, 3);

-- Insert data into Connections table
INSERT INTO `pay_my_buddy`.`connections` (`user_id`, `friend_id`) VALUES
                                                                      (1, 2),
                                                                      (1, 3),
                                                                      (2, 1),
                                                                      (2, 3);

-- Insert data into Transaction table
INSERT INTO `pay_my_buddy`.`transaction` (`id`, `sender`, `receiver`, `description`, `amount`, `date`, `type`) VALUES
-- VIREMENT RENTRANT (Compte en banque vers application)
(1, 1, 1, 'VIREMENT RENTRANT', 1500.00, '2024-01-01 10:00:00', 'VIREMENT RENTRANT'),
(2, 2, 2, 'VIREMENT RENTRANT', 2000.00, '2024-01-02 11:00:00', 'VIREMENT RENTRANT'),
(3, 3, 3, 'VIREMENT RENTRANT', 1000.00, '2024-01-03 12:00:00', 'VIREMENT RENTRANT'),

-- VIREMENT SORTANT (Application vers compte en banque)
(4, 1, 1, 'VIREMENT SORTANT', -200.00, '2024-01-04 13:00:00', 'VIREMENT SORTANT'),
(5, 2, 2, 'VIREMENT SORTANT', -500.00, '2024-01-05 14:00:00', 'VIREMENT SORTANT'),
(6, 3, 3, 'VIREMENT SORTANT', -300.00, '2024-01-06 15:00:00', 'VIREMENT SORTANT'),

-- TRANSFERT ENTRANT (Ami vers utilisateur)
(7, 2, 1, 'Remboursement du dîner', 100.00, '2024-01-07 16:00:00', 'TRANSFERT ENTRANT'),
(8, 3, 1, 'Partage des frais de taxi', 75.00, '2024-01-08 17:00:00', 'TRANSFERT ENTRANT'),
(9, 1, 2, 'Paiement pour les billets de concert', 50.00, '2024-01-09 18:00:00', 'TRANSFERT ENTRANT'),
(10, 3, 2, 'Remboursement pour le déjeuner', 30.00, '2024-01-10 19:00:00', 'TRANSFERT ENTRANT'),

-- TRANSFERT SORTANT (Utilisateur vers ami)
(11, 1, 2, 'Participation au cadeau commun', -40.00, '2024-01-11 20:00:00', 'TRANSFERT SORTANT'),
(12, 1, 3, 'Remboursement des courses', -75.00, '2024-01-12 21:00:00', 'TRANSFERT SORTANT'),
(13, 2, 3, 'Part des frais du voyage', -25.00, '2024-01-13 22:00:00', 'TRANSFERT SORTANT'),
(14, 2, 1, 'Paiement pour un restaurant', -50.00, '2024-01-14 23:00:00', 'TRANSFERT SORTANT');


SET FOREIGN_KEY_CHECKS = 1;