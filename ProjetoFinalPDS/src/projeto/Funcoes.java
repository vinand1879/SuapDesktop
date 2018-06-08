/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Solange
 */
public class Funcoes {

    public void cadastrarTurma(Turma turma) throws SQLException {
        Connection connection = new ConnectionFactory().getConnection();
        //Adicionando a turma
        String sql = "insert into turma (nome,turno,ano) values (?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, turma.getNome());
        stmt.setString(2, turma.getTurno());
        stmt.setInt(3, turma.getAno());

        stmt.execute();
        stmt.close();

        //Pegando o código da última turma da tabela, que no caso é essa que acabamos de adicionar
        String sql2 = "SELECT cod_turma FROM projeto.turma WHERE cod_turma = (SELECT MAX(cod_turma) FROM turma);";
        PreparedStatement stmt2 = connection.prepareStatement(sql2);
        ResultSet rs = stmt2.executeQuery();

        int turmaAdicionada = 0;
        while (rs.next()) {
            turmaAdicionada = rs.getInt("cod_turma");
            /*for (int i = 0; i < 12; i++) {
                System.out.println(turma.getListIndex(i));
            }*/
        }
        //preenchendo a tabela turma_discs com as disciplinas dessa turma
        for (int i = 0; i < 12; i++) {
            if (turma.getListIndex(i)) {
                String sql3 = "insert into turma_discs (cod_turma,cod_disc) values (?,?)";
                PreparedStatement stmt3 = connection.prepareStatement(sql3);

                stmt3.setInt(1, turmaAdicionada);
                stmt3.setInt(2, i + 1);

                stmt3.execute();
                stmt3.close();
            }
        }

        connection.close();
    }

    public ArrayList<Turma> retornaTurmas() {
        ArrayList<Turma> lista = new ArrayList<Turma>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "Select * FROM turma";
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Turma turma = new Turma();

                //Pegando os dados
                int cod_turma = rs.getInt("cod_turma");
                String nome = rs.getString("nome");
                String turno = rs.getString("turno");
                int ano = rs.getInt("ano");

                turma.setCod(cod_turma);
                turma.setNome(nome);
                turma.setTurno(turno);
                turma.setAno(ano);
                lista.add(turma);

            }
            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public ArrayList<Disciplina> retornaDiscsTurma(int turma) {

        ArrayList<Disciplina> lista = new ArrayList<Disciplina>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select disc.cod_disc, disc.nome from turma_discs turdi\n"
                + "inner join disciplina disc on turdi.cod_disc = disc.cod_disc\n"
                + "inner join turma on turma.cod_turma = turdi.cod_turma and turdi.cod_turma=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, turma);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                int cod_disc = rs.getInt("cod_disc");
                String nome = rs.getString("nome");

                disciplina.setCod(cod_disc);
                disciplina.setNome(nome);
                lista.add(disciplina);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<Cidade> retornaCidades() {
        ArrayList<Cidade> lista = new ArrayList<Cidade>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "SELECT * FROM projeto.cidade;";

        try {

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cidade cidade = new Cidade();
                int id_cidade = rs.getInt("id_cidade");
                String nome = rs.getString("nome");

                cidade.setId(id_cidade);
                cidade.setNome(nome);
                lista.add(cidade);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public void cadatraAluno(Aluno aluno) throws SQLException {
        Connection connection = new ConnectionFactory().getConnection();
        //Adicionando a turma
        String sql = "insert into aluno (nome,sexo,idade,cod_turma,id_cidade) values (?,?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, aluno.getNome());
        stmt.setString(2, aluno.getSexo());
        stmt.setInt(3, aluno.getIdade());
        stmt.setInt(4, aluno.getTurma());
        stmt.setInt(5, aluno.getCidade());

        stmt.execute();
        stmt.close();

        //Pegando a matricula do último aluno da tabela, que no caso é esse que acabamos de adicionar
        String sql2 = "SELECT matricula FROM projeto.aluno WHERE matricula = (SELECT MAX(matricula) FROM aluno);";
        PreparedStatement stmt2 = connection.prepareStatement(sql2);
        ResultSet rs = stmt2.executeQuery();

        int alunoAdicionado = 0;
        while (rs.next()) {
            alunoAdicionado = rs.getInt("matricula");
        }

        //setando todas as notas desse aluno (em todas as disciplinas dele) para 0
        ArrayList<Disciplina> disciplinas = retornaDiscsTurma(retornaTurmaAluno(alunoAdicionado));

        for (Disciplina d : disciplinas) {
            String sql3 = "insert into projeto.paga (matricula_aluno,cod_disc,nota1,nota2,nota3,nota4) values (?,?,?,?,?,?);";
            PreparedStatement stmt3 = connection.prepareStatement(sql3);

            stmt3.setInt(1, alunoAdicionado);
            stmt3.setInt(2, d.getCod());
            stmt3.setInt(3, 0);
            stmt3.setInt(4, 0);
            stmt3.setInt(5, 0);
            stmt3.setInt(6, 0);

            stmt3.execute();
            stmt3.close();
        }

        connection.close();
    }

    public ArrayList<Aluno> retornaAlunosTurma(int turma) {
        ArrayList<Aluno> lista = new ArrayList<Aluno>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select * from aluno where cod_turma=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, turma);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Aluno aluno = new Aluno();
                int matricula = rs.getInt("matricula");
                String nome = rs.getString("nome");
                String sexo = rs.getString("sexo");
                int idade = rs.getInt("idade");
                int cidade = rs.getInt("id_cidade");

                aluno.setMatricula(matricula);
                aluno.setNome(nome);
                aluno.setSexo(sexo);
                aluno.setIdade(idade);
                aluno.setCidade(cidade);
                lista.add(aluno);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public Notas retornaNotasAlunoDisc(int matAluno, int codisc) {
        Notas notas = new Notas();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select * from paga where matricula_aluno=? and cod_disc =?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matAluno);
            stmt.setInt(2, codisc);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notas.setMatAluno(rs.getInt("matricula_aluno"));
                notas.setCodisc(rs.getInt("cod_disc"));
                notas.setNota1(rs.getInt("nota1"));
                notas.setNota2(rs.getInt("nota2"));
                notas.setNota3(rs.getInt("nota3"));
                notas.setNota4(rs.getInt("nota4"));
            }

            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public int retornaTurmaAluno(int matAluno) {
        Connection connection = new ConnectionFactory().getConnection();
        String sql = "select cod_turma from projeto.aluno where matricula = ?;";
        int turma = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                turma = rs.getInt("cod_turma");
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return turma;
    }

    public void cadastraNotas(Notas notas) throws SQLException {
        Connection connection = new ConnectionFactory().getConnection();
        String sql = "update projeto.paga set nota1=?, nota2=?, nota3=?, nota4=?\n"
                + "where matricula_aluno=? and cod_disc=?;";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setInt(1, notas.getNota1());
        stmt.setInt(2, notas.getNota2());
        stmt.setInt(3, notas.getNota3());
        stmt.setInt(4, notas.getNota4());
        stmt.setInt(5, notas.getMatAluno());
        stmt.setInt(6, notas.getCodisc());

        stmt.execute();
        stmt.close();
        connection.close();
    }

    public void CadastraProfessor(Professor prof) throws SQLException {
        Connection connection = new ConnectionFactory().getConnection();
        //Adicionando a turma
        String sql = "insert into professor (nome,idade,sexo,titulo,id_cidade) values (?,?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, prof.getNome());
        stmt.setInt(2, prof.getIdade());
        stmt.setString(3, prof.getSexo());
        stmt.setString(4, prof.getTitulo());
        stmt.setInt(5, prof.getCidade());

        stmt.execute();
        stmt.close();

        //Pegando a matricula do último professor da tabela, que no caso é esse que acabamos de adicionar
        String sql2 = "SELECT matricula_prof FROM projeto.professor WHERE matricula_prof"
                + " = (SELECT MAX(matricula_prof) FROM professor);";
        PreparedStatement stmt2 = connection.prepareStatement(sql2);
        ResultSet rs = stmt2.executeQuery();

        int profAdicionado = 0;
        while (rs.next()) {
            profAdicionado = rs.getInt("matricula_prof");
        }
        //preenchendo a tabela prof_discs com as disciplinas desse professor
        for (int i = 0; i < 12; i++) {
            if (prof.getListIndex(i)) {
                String sql3 = "insert into prof_discs (matricula_prof,cod_disc) values (?,?)";
                PreparedStatement stmt3 = connection.prepareStatement(sql3);

                stmt3.setInt(1, profAdicionado);
                stmt3.setInt(2, i + 1);

                stmt3.execute();
                stmt3.close();
            }
        }

        connection.close();
    }

    public ArrayList<Professor> retornaProfessores() {
        ArrayList<Professor> lista = new ArrayList<Professor>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select * from professor;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Professor professor = new Professor();
                int matricula = rs.getInt("matricula_prof");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String sexo = rs.getString("sexo");
                String titulo = rs.getString("titulo");
                int cidade = rs.getInt("id_cidade");

                professor.setMatricula(matricula);
                professor.setNome(nome);
                professor.setIdade(idade);
                professor.setSexo(sexo);
                professor.setTitulo(titulo);
                professor.setCidade(cidade);
                lista.add(professor);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }

    public ArrayList<Disciplina> retornaDiscsProf(int prof) {

        ArrayList<Disciplina> lista = new ArrayList<Disciplina>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select disc.cod_disc, disc.nome from prof_discs prodi\n"
                + "inner join disciplina disc on prodi.cod_disc = disc.cod_disc\n"
                + "inner join professor prof on prof.matricula_prof = prodi.matricula_prof and prodi.matricula_prof=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, prof);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Disciplina disciplina = new Disciplina();
                int cod_disc = rs.getInt("cod_disc");
                String nome = rs.getString("nome");

                disciplina.setCod(cod_disc);
                disciplina.setNome(nome);
                lista.add(disciplina);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public void cadastrarAula(Aula aula) {
        Connection connection = new ConnectionFactory().getConnection();
        String sql = "insert into aula(data, assunto, matricula_prof, cod_disc, cod_turma) values(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, aula.getData());
            stmt.setString(2, aula.getAssunto());
            stmt.setInt(3, aula.getMatProf());
            stmt.setInt(4, aula.getCodisc());
            stmt.setInt(5, aula.getCodTurma());

            stmt.execute();
            stmt.close();

            //Pegando o codigo da última aula da tabela, que no caso é essa que acabamos de adicionar
            String sql2 = "SELECT cod_aula FROM projeto.aula WHERE cod_aula = (SELECT MAX(cod_aula) FROM projeto.aula);";
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            ResultSet rs = stmt2.executeQuery();

            int aulaAdicionada = 0;
            while (rs.next()) {
                aulaAdicionada = rs.getInt("cod_aula");
            }

            ArrayList<Integer> lista1 = aula.getMatAlunos();
            ArrayList<String> lista2 = aula.getStatus();

            for (int i = 0; i < lista1.size(); i++) {
                String sql3 = "insert into frequencia (matricula_aluno, cod_aula, presenca) values (?,?,?)";
                PreparedStatement stmt3 = connection.prepareStatement(sql3);

                stmt3.setInt(1, lista1.get(i));
                stmt3.setInt(2, aulaAdicionada);
                stmt3.setString(3, lista2.get(i));

                stmt3.execute();
                stmt3.close();
            }
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Notas> retornaNotasAluno(int matAluno) {
        ArrayList<Notas> notas = new ArrayList<Notas>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select disc.nome,p.cod_disc, p.nota1, p.nota2, p.nota3, p.nota4 from paga p\n"
                + "inner join disciplina disc on p.cod_disc = disc.cod_disc and p.matricula_aluno = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Notas nota = new Notas();
                nota.setNomeDisc(rs.getString("nome"));
                nota.setCodisc(rs.getInt("cod_disc"));
                nota.setNota1(rs.getInt("nota1"));
                nota.setNota2(rs.getInt("nota2"));
                nota.setNota3(rs.getInt("nota3"));
                nota.setNota4(rs.getInt("nota4"));

                notas.add(nota);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notas;
    }

    public ArrayList<Aula> retornaAulas(int codTurma, int codisc) {
        ArrayList<Aula> lista = new ArrayList<Aula>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select * from projeto.aula where cod_turma = ? and cod_disc = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codTurma);
            stmt.setInt(2, codisc);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Aula aula = new Aula();
                aula.setCodAula(rs.getInt("cod_aula"));
                aula.setData(rs.getString("data"));
                aula.setAssunto(rs.getString("assunto"));
                aula.setMatProf(rs.getInt("matricula_prof"));
                aula.setCodisc(rs.getInt("cod_disc"));
                aula.setCodTurma(rs.getInt("cod_turma"));

                lista.add(aula);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public ArrayList<Frequencia> retornaFrequencia(int codAula) {
        ArrayList<Frequencia> lista = new ArrayList<Frequencia>();
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "select al.nome, feq.matricula_aluno, feq.presenca from frequencia feq inner join\n"
                + "aluno al on feq.matricula_aluno = al.matricula and cod_aula = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codAula);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Frequencia frequencia = new Frequencia();
                frequencia.setNomeAluno(rs.getString("nome"));
                frequencia.setPresenca(rs.getString("presenca"));
                frequencia.setMatAluno(rs.getInt("matricula_aluno"));
                
                lista.add(frequencia);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public void apagarAluno(int matricula){
        Connection connection = new ConnectionFactory().getConnection();

        String sql2 = "delete from paga where matricula_aluno = ?;";
        String sql3 = "delete from frequencia where matricula_aluno = ?;";
        String sql = "delete from aluno where matricula = ?;";
        
        try {            
            PreparedStatement stmt2 = connection.prepareStatement(sql3);
            stmt2.setInt(1, matricula);
            stmt2.execute();
            stmt2.close();
            
            PreparedStatement stmt3 = connection.prepareStatement(sql2);
            stmt3.setInt(1, matricula);
            
            stmt3.execute();
            stmt3.close();
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matricula);
            stmt.execute();
            stmt.close();
            
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void apagarProfessor(int matricula){
        Connection connection = new ConnectionFactory().getConnection();

        String sql = "delete from prof_discs where matricula_prof = ?;";
        String sql3 = "delete from professor where matricula_prof = ?;";
        
        try {
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, matricula);
            
            stmt.execute();
            stmt.close();
            
            PreparedStatement stmt3 = connection.prepareStatement(sql3);
            stmt3.setInt(1, matricula);
            stmt3.execute();
            stmt3.close();
            
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Funcoes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
