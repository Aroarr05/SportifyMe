import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearDesafio } from './crear-desafio';

describe('CrearDesafio', () => {
  let component: CrearDesafio;
  let fixture: ComponentFixture<CrearDesafio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearDesafio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearDesafio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
