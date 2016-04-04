package hr.m2stanic.smartbuilding.web;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.SimpleCaptchaServlet;
import nl.captcha.text.renderer.ColoredEdgesWordRenderer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class EkipCaptcha extends SimpleCaptchaServlet {

    private static int _width = 200;
    private static int _height = 50;

    private static final java.util.List<Color> COLORS = new ArrayList<>(2);
    private static final java.util.List<Font> FONTS = new ArrayList<>(3);

    static {
        COLORS.add(Color.BLACK);
        COLORS.add(Color.BLUE);

        FONTS.add(new Font("Geneva", Font.ITALIC, 48));
        FONTS.add(new Font("Courier", Font.BOLD, 48));
        FONTS.add(new Font("Arial", Font.BOLD, 48));
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (getInitParameter("captcha-height") != null) {
            _height = Integer.valueOf(getInitParameter("captcha-height"));
        }

        if (getInitParameter("captcha-width") != null) {
            _width = Integer.valueOf(getInitParameter("captcha-width"));
        }
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ColoredEdgesWordRenderer wordRenderer = new ColoredEdgesWordRenderer(COLORS, FONTS);
//        Captcha captcha = new Captcha.Builder(_width, _height).addText(wordRenderer)
//                .gimp()
//                .addNoise()
//                .addBackground(new GradiatedBackgroundProducer())
//                .build();

        Captcha captcha = new Captcha.Builder(_width, _height)
                .addText()
                .addNoise()
                .build();

        CaptchaServletUtil.writeImage(resp, captcha.getImage());
        req.getSession().setAttribute(Captcha.NAME, captcha);

    }
}