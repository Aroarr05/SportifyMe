import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleDesafio } from './detalle-desafio';

describe('DetalleDesafio', () => {
  let component: DetalleDesafio;
  let fixture: ComponentFixture<DetalleDesafio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleDesafio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleDesafio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
