// Mobile Menu Toggle
document.addEventListener('DOMContentLoaded', function() {
  const menuBtn = document.querySelector('.mobile-menu-btn');
  const topnav = document.querySelector('.topnav');
  
  if (menuBtn && topnav) {
    menuBtn.addEventListener('click', function(e) {
      e.preventDefault();
      topnav.classList.toggle('active');
      menuBtn.classList.toggle('active');
    });
    
    // Close menu when a link is clicked
    const navLinks = topnav.querySelectorAll('a');
    navLinks.forEach(link => {
      link.addEventListener('click', function() {
        topnav.classList.remove('active');
        menuBtn.classList.remove('active');
      });
    });
    
    // Close menu when clicking outside
    document.addEventListener('click', function(e) {
      if (!e.target.closest('.topbar')) {
        topnav.classList.remove('active');
        menuBtn.classList.remove('active');
      }
    });
  }
  
  // Fix form input zoom on iOS
  const inputs = document.querySelectorAll('input, textarea, select');
  inputs.forEach(input => {
    input.addEventListener('focus', function() {
      if (window.innerWidth < 768) {
        // Allow natural zoom on mobile
        this.style.fontSize = '16px';
      }
    });
  });
  
  // Smooth scroll for anchor links
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
      const href = this.getAttribute('href');
      if (href !== '#') {
        e.preventDefault();
        const target = document.querySelector(href);
        if (target) {
          target.scrollIntoView({ behavior: 'smooth' });
        }
      }
    });
  });
  
  // Add touch feedback for buttons
  const buttons = document.querySelectorAll('button, .btn, .cart-btn, .qty-btn');
  buttons.forEach(button => {
    button.addEventListener('touchstart', function() {
      this.style.opacity = '0.8';
    });
    button.addEventListener('touchend', function() {
      this.style.opacity = '1';
    });
  });
  
  // Responsive viewport adjustments
  const metaViewport = document.querySelector('meta[name="viewport"]');
  if (metaViewport) {
    metaViewport.setAttribute('content', 'width=device-width, initial-scale=1.0, viewport-fit=cover');
  }
});

// Debounce function for resize events
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

// Handle window resize
window.addEventListener('resize', debounce(function() {
  // Close mobile menu on resize to desktop
  if (window.innerWidth > 768) {
    const topnav = document.querySelector('.topnav');
    const menuBtn = document.querySelector('.mobile-menu-btn');
    if (topnav) topnav.classList.remove('active');
    if (menuBtn) menuBtn.classList.remove('active');
  }
}, 250));

// Prevent zoom on double tap
let lastTouchEnd = 0;
document.addEventListener('touchend', function(event) {
  const now = Date.now();
  if (now - lastTouchEnd <= 300) {
    event.preventDefault();
  }
  lastTouchEnd = now;
}, false);
