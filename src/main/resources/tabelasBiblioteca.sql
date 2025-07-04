CREATE TABLE IF NOT EXISTS livro (
    ISBN INTEGER PRIMARY KEY,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    dataPublicacao DATE,
    numCopias INTEGER
);
CREATE TABLE IF NOT EXISTS membro (
    idMembro INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    telefone TEXT,
    email TEXT,
    devendo BOOLEAN DEFAULT 0
);
CREATE TABLE IF NOT EXISTS emprestimoLivro (
    idEmprestimo INTEGER PRIMARY KEY AUTOINCREMENT,
    isDisponivel BOOLEAN NOT NULL,
    dataEmprestimo DATE,
    multaCalculo REAL, -- <-- No SQlite, REAL = FLOAT, não é real de reais.
    dataDevolucao DATE,
    idMembro INTEGER,
    ISBN INTEGER,
    FOREIGN KEY (idMembro) REFERENCES membro(idMembro),
    FOREIGN KEY (ISBN) REFERENCES livro(ISBN)
);