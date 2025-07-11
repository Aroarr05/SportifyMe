import { TestBed } from '@angular/core/testing';

import { Desafios } from '../desafios';

describe('Desafios', () => {
  let service: Desafios;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Desafios);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
