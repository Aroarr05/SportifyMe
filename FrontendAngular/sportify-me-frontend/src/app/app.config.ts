import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideClientHydration } from '@angular/platform-browser';
import { routes } from './app.routes';

// Si usas clase interceptor, NO uses withInterceptors()
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(), // ← Sin withInterceptors
    provideClientHydration()
    // Y añade el interceptor como provider tradicional si es clase
  ]
};