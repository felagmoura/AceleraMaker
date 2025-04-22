import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { PostsComponent } from './pages/posts/posts.component';
import { WriteComponent } from './pages/write/write.component'
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {
    path: 'posts',
    component: PostsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'write',
    component: WriteComponent,
    canActivate: [authGuard]
  }
];
