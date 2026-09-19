-- ===================================================================
-- PokéSal Simulator - Script de Seed (Carga Inicial)
-- Contém 12 PokéSals e 4 Treinadores (conforme requisitos da disciplina)
-- ===================================================================

-- Garante que a coluna de moedas exista caso a tabela já tenha sido criada previamente
ALTER TABLE IF EXISTS treinadores ADD COLUMN IF NOT EXISTS moedas INT NOT NULL DEFAULT 100;

-- Limpa dados prévios garantindo integridade referencial
TRUNCATE TABLE treinadores CASCADE;
TRUNCATE TABLE pokesals RESTART IDENTITY CASCADE;

-- Inserção de 12 PokéSals com atributos equilibrados
INSERT INTO pokesals (id, nome, tipo, hp_max, hp_atual, ataque, defesa, velocidade) VALUES
(1,  'Salmander',   'FOGO',     100, 100, 32, 18, 52),
(2,  'Salvasaur',   'PLANTA',   110, 110, 25, 24, 45),
(3,  'Salstoise',   'AGUA',     105, 105, 22, 30, 43),
(4,  'Rapisalt',    'ELETRICO',  95,  95, 30, 16, 75),
(5,  'Pigeosal',    'NORMAL',    90,  90, 26, 18, 65),
(6,  'Rattasal',    'NORMAL',    85,  85, 28, 15, 70),
(7,  'Arcasal',     'FOGO',     120, 120, 38, 22, 60),
(8,  'Polisal',     'AGUA',     115, 115, 26, 25, 50),
(9,  'Vilesal',     'PLANTA',   105, 105, 29, 26, 40),
(10, 'Electrosal',  'ELETRICO',  90,  90, 36, 20, 80),
(11, 'Snorlasal',   'NORMAL',   160, 160, 35, 32, 20),
(12, 'Gyaradosal',  'AGUA',     130, 130, 42, 28, 55);

-- Ajusta a sequência de IDs da tabela pokesals
SELECT setval('pokesals_id_seq', (SELECT MAX(id) FROM pokesals));

-- Inserção de 4 Treinadores vinculados a seus respectivos parceiros iniciais e com saldo de PokéMoedas
INSERT INTO treinadores (id, nome, pokesal_ativo_id, moedas) VALUES
(1, 'Ash UCSAL', 1, 100),
(2, 'Gary UCSAL', 3, 150),
(3, 'Misty Pituba', 8, 120),
(4, 'Brock Lapa', 2, 200);

-- Ajusta a sequência de IDs da tabela treinadores
SELECT setval('treinadores_id_seq', (SELECT MAX(id) FROM treinadores));
