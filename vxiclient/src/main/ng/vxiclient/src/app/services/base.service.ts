import { Injectable } from '@angular/core';

export class BaseService {

  constructor() { }

  protected handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }

}
