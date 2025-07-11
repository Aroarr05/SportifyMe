import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaDesafios } from './lista-desafios';

describe('ListaDesafios', () => {
  let component: ListaDesafios;
  let fixture: ComponentFixture<ListaDesafios>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaDesafios]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaDesafios);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
