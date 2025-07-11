import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrarProgreso } from './registrar-progreso';

describe('RegistrarProgreso', () => {
  let component: RegistrarProgreso;
  let fixture: ComponentFixture<RegistrarProgreso>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrarProgreso]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrarProgreso);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
