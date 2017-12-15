import { ImageBrowserPage } from './app.po';

describe('image-browser App', () => {
  let page: ImageBrowserPage;

  beforeEach(() => {
    page = new ImageBrowserPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
