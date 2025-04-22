export interface Post {
  id: string;
  titulo: string;
  texto: string;
  usuarioId: string;
  temaId: string;
  dataCriacao: Date;
  isDraft: boolean;
}
