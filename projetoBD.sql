create database projeto;
use projeto;

create table turma(
	cod_turma int not null primary key auto_increment,
    nome varchar(60),
    turno varchar(10),
    ano int
);

create table disciplina(
	cod_disc int not null primary key auto_increment,
    nome varchar(60)
);

create table cidade(
	id_cidade int not null primary key auto_increment,
    nome varchar(60)
);

create table aluno(
	matricula int not null primary key auto_increment,
    nome varchar(60),
    sexo varchar(9),
    idade int,
    cod_turma int,
    id_cidade int,
    foreign key(cod_turma) references turma (cod_turma),
    foreign key(id_cidade) references cidade (id_cidade)
);

create table professor(
	matricula_prof int not null primary key auto_increment,
    nome varchar(60),
    idade int,
    sexo varchar(15),
    titulo varchar(60),
    id_cidade int,
    foreign key(id_cidade) references cidade (id_cidade)
);

create table aula(
	cod_aula int not null primary key auto_increment,
    data varchar(10),
    assunto varchar(100),
    matricula_prof int,
    cod_disc int,
	cod_turma int,
	foreign key(cod_turma) references turma (cod_turma),
    foreign key(cod_disc) references disciplina (cod_disc),
    foreign key(matricula_prof) references professor (matricula_prof)
);

create table frequencia(
	matricula_aluno int,
    cod_aula int,
	presenca varchar(20),
    foreign key(matricula_aluno) references aluno(matricula),
    foreign key(cod_aula) references aula(cod_aula)
);

create table paga(
	matricula_aluno int,
    cod_disc int,
    nota1 int,
	nota2 int,
	nota3 int,
	nota4 int,
    foreign key(cod_disc) references disciplina (cod_disc),
    foreign key(matricula_aluno) references aluno(matricula)
);

create table turma_discs(
	cod_turma int,
	cod_disc int,
	foreign key(cod_turma) references turma (cod_turma),
    foreign key(cod_disc) references disciplina (cod_disc)
);

create table prof_discs(
	matricula_prof int,
	cod_disc int,
	foreign key(matricula_prof) references professor (matricula_prof),
    foreign key(cod_disc) references disciplina (cod_disc)
);

insert into disciplina(nome) values
('Português'),
('Matemática'),
('Física'),
('Biologia'),
('Química'),
('Geografia'),
('História'),
('Filosofia'),
('Sociologia'),
('Ed. Física'),
('Inglês'),
('Literatura');


insert into cidade(nome) values
('São Miguel'),
('Pau dos Ferros'),
('Portalegre'),
('Alexandria'),
('José da Penha'),
('Tabuleiro Grande'),
('Serrinha dos Pintos');

/*-- pega todas as notas dos alunos da turma 2
select al.nome,p.cod_disc, p.nota1, p.nota2, p.nota3, p.nota4 from paga p
inner join aluno al on p.matricula_aluno = al.matricula and al.cod_turma = 2;

-- Pega todas as notas (com os nomes das matérias) do aluno de id=1
select disc.nome,p.cod_disc, p.nota1, p.nota2, p.nota3, p.nota4 from paga p
inner join disciplina disc on p.cod_disc = disc.cod_disc and p.matricula_aluno = 1;
*/
