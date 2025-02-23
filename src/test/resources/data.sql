-- Insert test users
INSERT INTO users (id, username, email, password) VALUES
                                                      (1, 'testUser', 'test@test.com', '$2a$12$DrXsVXsSy4Ndmbudp86BK.xWS5gwsEGtucbyuE4cFR0asiVCnFhTC'),
                                                      (2, 'testUser2', 'test2@test.com', '$2a$12$5I7Ivj9x0hPBdpvAWlzfmum8VQv5Md5Pfo8G21QposY.p92Wk6H.q');

-- Create accounts for users
INSERT INTO account (user_id) VALUES
                                  (1),
                                  (2);

-- Create connection between users
INSERT INTO connections (user_id, friend_id) VALUES
                                                 (1, 2),
                                                 (2, 1);

-- Add some initial transactions
INSERT INTO transaction (sender, receiver, description, amount, fee, type, date) VALUES
                                                                                     (1, 1, 'Initial deposit', 1000.00, 0.00, 'VIREMENT ENTRANT', CURRENT_TIMESTAMP()),
                                                                                     (2, 2, 'Initial deposit', 1000.00, 0.00, 'VIREMENT ENTRANT', CURRENT_TIMESTAMP());