import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { livroService } from '../services/api';

function LivroList() {
  const [livros, setLivros] = useState([]);
  const [erro, setErro] = useState('');

  const carregar = () => {
    livroService
      .listar()
      .then((res) => setLivros(res.data))
      .catch(() => setErro('Erro ao carregar os livros.'));
  };

  useEffect(carregar, []);

  const excluir = (id) => {
    if (!window.confirm('Deseja realmente excluir este livro?')) return;
    livroService
      .deletar(id)
      .then(() => {
        setErro('');
        carregar();
      })
      .catch(() => setErro('Erro ao excluir o livro.'));
  };

  return (
    <div>
      <div className="header-row">
        <h2>Livros</h2>
        <Link to="/livros/novo" className="btn">+ Novo livro</Link>
      </div>

      {erro && <div className="erro">{erro}</div>}

      <table>
        <thead>
          <tr>
            <th>Título</th>
            <th>Autor</th>
            <th>Ano</th>
            <th>Gênero</th>
            <th>ISBN</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {livros.map((l) => (
            <tr key={l.id}>
              <td>{l.titulo}</td>
              <td>{l.autor ? l.autor.nome : '-'}</td>
              <td>{l.anoPublicacao || '-'}</td>
              <td>{l.genero || '-'}</td>
              <td>{l.isbn || '-'}</td>
              <td>
                <Link to={`/livros/editar/${l.id}`} className="btn">Editar</Link>
                <button className="btn danger" onClick={() => excluir(l.id)}>Excluir</button>
              </td>
            </tr>
          ))}
          {livros.length === 0 && (
            <tr>
              <td colSpan="6">Nenhum livro cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default LivroList;
