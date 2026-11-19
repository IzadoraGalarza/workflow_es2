import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { autorService } from '../services/api';

function AutorList() {
  const [autores, setAutores] = useState([]);
  const [erro, setErro] = useState('');

  const carregar = () => {
    autorService
      .listar()
      .then((res) => setAutores(res.data))
      .catch(() => setErro('Erro ao carregar os autores.'));
  };

  useEffect(carregar, []);

  const excluir = (id) => {
    if (!window.confirm('Deseja realmente excluir este autor?')) return;
    autorService
      .deletar(id)
      .then(() => {
        setErro('');
        carregar();
      })
      .catch((e) => {
        if (e.response && e.response.status === 409) {
          setErro('Não é possível excluir: o autor possui livros. Exclua os livros antes.');
        } else {
          setErro('Erro ao excluir o autor.');
        }
      });
  };

  const formatarData = (data) => {
    if (!data) return '-';
    const [ano, mes, dia] = data.split('-');
    return `${dia}/${mes}/${ano}`;
  };

  return (
    <div>
      <div className="header-row">
        <h2>Autores</h2>
        <Link to="/autores/novo" className="btn">+ Novo autor</Link>
      </div>

      {erro && <div className="erro">{erro}</div>}

      <table>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Nacionalidade</th>
            <th>Nascimento</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {autores.map((a) => (
            <tr key={a.id}>
              <td>{a.nome}</td>
              <td>{a.nacionalidade || '-'}</td>
              <td>{formatarData(a.dataNascimento)}</td>
              <td>
                <Link to={`/autores/editar/${a.id}`} className="btn">Editar</Link>
                <button className="btn danger" onClick={() => excluir(a.id)}>Excluir</button>
              </td>
            </tr>
          ))}
          {autores.length === 0 && (
            <tr>
              <td colSpan="4">Nenhum autor cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default AutorList;
