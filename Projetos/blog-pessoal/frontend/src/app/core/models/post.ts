export interface Post {
  id: number;
  titulo: string;
  texto: string;
  usuarioId: number;
  temaId?: number;
  dataCriacao: Date;
  isDraft: boolean;
}
